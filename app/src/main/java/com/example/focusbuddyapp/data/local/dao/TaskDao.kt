package com.example.focusbuddyapp.data.local.dao

import androidx.room.*
import com.example.focusbuddyapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // ── BREAD: Browse ────────────────────────────────────────────────────────
    @Query("SELECT * FROM tasks ORDER BY is_completed ASC, due_date ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    // ── BREAD: Read ──────────────────────────────────────────────────────────
    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    fun getTaskByIdFlow(id: Int): Flow<TaskEntity?>

    // ── BREAD: Add ───────────────────────────────────────────────────────────
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    // ── BREAD: Edit ──────────────────────────────────────────────────────────
    @Update
    suspend fun updateTask(task: TaskEntity)

    // ── BREAD: Delete ────────────────────────────────────────────────────────
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    // ── Extras ───────────────────────────────────────────────────────────────
    @Query("UPDATE tasks SET is_completed = :isCompleted, completed_at = :completedAt, progress_percent = CASE WHEN :isCompleted = 1 THEN 100 ELSE 0 END WHERE id = :id")
    suspend fun toggleTaskComplete(id: Int, isCompleted: Boolean, completedAt: Long?)

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY due_date ASC")
    fun searchTasks(query: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY due_date ASC")
    fun getTasksByPriority(priority: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE due_date >= :startOfDay AND due_date < :endOfDay ORDER BY due_date ASC")
    fun getTodayTasks(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE is_completed = 1 AND completed_at >= :startOfDay AND completed_at < :endOfDay ORDER BY completed_at DESC")
    fun getCompletedTodayTasks(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE user_id = :userId")
    suspend fun getTaskCountForUser(userId: Int): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 1 AND completed_at >= :startOfDay AND completed_at < :endOfDay")
    fun getCompletedTodayCount(startOfDay: Long, endOfDay: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0")
    fun getPendingTaskCount(): Flow<Int>

    @Query("SELECT * FROM tasks WHERE remote_id IS NULL OR synced_at IS NULL")
    suspend fun getUnsyncedTasks(): List<TaskEntity>

    @Query("UPDATE tasks SET remote_id = :remoteId, synced_at = :syncedAt WHERE id = :id")
    suspend fun markSynced(id: Int, remoteId: String, syncedAt: Long)
}
