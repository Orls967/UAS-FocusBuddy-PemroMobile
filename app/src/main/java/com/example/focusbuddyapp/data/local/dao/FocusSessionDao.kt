package com.example.focusbuddyapp.data.local.dao

import androidx.room.*
import com.example.focusbuddyapp.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Update
    suspend fun updateSession(session: FocusSessionEntity)

    @Query("SELECT * FROM focus_sessions WHERE id = :id LIMIT 1")
    suspend fun getSessionById(id: Int): FocusSessionEntity?

    @Query("SELECT * FROM focus_sessions ORDER BY start_time DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE start_time >= :start ORDER BY start_time DESC")
    fun getTodaySessions(start: Long): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE start_time >= :start")
    fun getSessionsFromDate(start: Long): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE linked_task_id = :taskId")
    fun getSessionsForTask(taskId: Int): Flow<List<FocusSessionEntity>>

    @Query("SELECT SUM(duration_minutes) FROM focus_sessions WHERE start_time >= :start")
    fun getTodayFocusMinutes(start: Long): Flow<Int?>

    @Query("SELECT * FROM focus_sessions WHERE synced_at IS NULL")
    suspend fun getUnsyncedSessions(): List<FocusSessionEntity>
}
