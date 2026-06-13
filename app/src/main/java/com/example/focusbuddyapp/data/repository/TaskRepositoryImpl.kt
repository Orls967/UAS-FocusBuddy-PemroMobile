package com.example.focusbuddyapp.data.repository

import android.util.Log
import com.example.focusbuddyapp.data.local.dao.SubTaskDao
import com.example.focusbuddyapp.data.local.dao.TaskDao
import com.example.focusbuddyapp.data.mapper.toDomain
import com.example.focusbuddyapp.data.mapper.toEntity
import com.example.focusbuddyapp.data.remote.api.TaskApiService
import com.example.focusbuddyapp.data.remote.dto.TaskCreateRequestDto
import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao,
    private val taskApiService: TaskApiService
) : TaskRepository {

    // ── BREAD: Browse ─────────────────────────────────────────────────────────
    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }

    // ── BREAD: Read ───────────────────────────────────────────────────────────
    override suspend fun getTaskById(id: Int): Task? = withContext(Dispatchers.IO) {
        taskDao.getTaskById(id)?.toDomain()
    }

    // ── BREAD: Add ────────────────────────────────────────────────────────────
    override suspend fun addTask(task: Task): Long = withContext(Dispatchers.IO) {
        val id = taskDao.insertTask(task.toEntity())
        // Sync to remote in background (best-effort)
        syncTaskToRemote(task.copy(id = id.toInt()))
        id
    }

    // ── BREAD: Edit ───────────────────────────────────────────────────────────
    override suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.updateTask(task.toEntity())
        syncTaskToRemote(task)
    }

    // ── BREAD: Delete ─────────────────────────────────────────────────────────
    override suspend fun deleteTask(id: Int) = withContext(Dispatchers.IO) {
        val task = taskDao.getTaskById(id)
        task?.remoteId?.let {
            runCatching { taskApiService.deleteTask(it) }
        }
        taskDao.deleteTask(id)
    }

    override suspend fun toggleTaskComplete(id: Int, isCompleted: Boolean) =
        withContext(Dispatchers.IO) { taskDao.toggleTaskComplete(id, isCompleted) }

    override fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query).map { it.map { e -> e.toDomain() } }

    override fun getTasksByPriority(priority: String): Flow<List<Task>> =
        taskDao.getTasksByPriority(priority).map { it.map { e -> e.toDomain() } }

    override fun getTodayTasks(): Flow<List<Task>> {
        val (start, end) = todayBounds()
        return taskDao.getTodayTasks(start, end).map { it.map { e -> e.toDomain() } }
    }

    override fun getCompletedTodayCount(): Flow<Int> {
        val (start, end) = todayBounds()
        return taskDao.getCompletedTodayCount(start, end)
    }

    // ── Remote sync ───────────────────────────────────────────────────────────
    override suspend fun syncWithRemote(): Unit = withContext(Dispatchers.IO) {
        runCatching {
            val remoteTasks = taskApiService.getAllTasks()
            remoteTasks.forEach { dto ->
                val existing = taskDao.getUnsyncedTasks()
                // Upsert remote tasks to local
                val entity = dto.toDomain().toEntity()
                taskDao.insertTask(entity)
            }
        }.onFailure { Log.w("TaskRepo", "Sync failed: ${it.message}") }
        Unit
    }

    private suspend fun syncTaskToRemote(task: Task) {
        runCatching {
            val request = TaskCreateRequestDto(
                title = task.title, description = task.description,
                category = task.category, priority = task.priority.name,
                dueDate = task.dueDate, dueTime = task.dueTime, studyNotes = task.studyNotes
            )
            val response = if (task.remoteId != null) {
                taskApiService.updateTask(task.remoteId, request)
            } else {
                taskApiService.createTask(request)
            }
            taskDao.markSynced(task.id, response.id, System.currentTimeMillis())
        }.onFailure { Log.w("TaskRepo", "Remote sync skipped: ${it.message}") }
    }

    private fun todayBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        val end = start + 24 * 60 * 60 * 1000L
        return start to end
    }
}
