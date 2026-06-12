package com.example.focusbuddyapp.data.local.dao

import androidx.room.*
import com.example.focusbuddyapp.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE is_completed = :isCompleted ORDER BY id DESC")
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY id DESC")
    fun getTasksByPriority(priority: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE due_date >= :start AND due_date < :end")
    fun getTodayTasks(start: Long, end: Long): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 1 AND due_date >= :start AND due_date < :end")
    fun getCompletedTodayCount(start: Long, end: Long): Flow<Int>

    @Query("UPDATE tasks SET is_completed = :isCompleted WHERE id = :id")
    suspend fun toggleTaskComplete(id: Int, isCompleted: Boolean)

    @Query("SELECT * FROM tasks WHERE synced_at IS NULL")
    suspend fun getUnsyncedTasks(): List<TaskEntity>

    @Query("UPDATE tasks SET remote_id = :remoteId, synced_at = :syncedAt WHERE id = :id")
    suspend fun markSynced(id: Int, remoteId: String, syncedAt: Long)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)
}
