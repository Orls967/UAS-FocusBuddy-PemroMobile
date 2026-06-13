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

    @Query("SELECT * FROM focus_sessions WHERE start_time >= :startOfDay AND start_time < :endOfDay")
    fun getTodaySessions(startOfDay: Long, endOfDay: Long): Flow<List<FocusSessionEntity>>

    @Query("SELECT COALESCE(SUM(duration_minutes), 0) FROM focus_sessions WHERE start_time >= :startOfDay AND start_time < :endOfDay AND end_time IS NOT NULL")
    fun getTodayFocusMinutes(startOfDay: Long, endOfDay: Long): Flow<Int>

    @Query("SELECT * FROM focus_sessions ORDER BY start_time DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE linked_task_id = :taskId")
    fun getSessionsForTask(taskId: Int): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE start_time >= :weekStart AND end_time IS NOT NULL ORDER BY start_time ASC")
    fun getSessionsThisWeek(weekStart: Long): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE end_time IS NULL LIMIT 1")
    suspend fun getActiveSession(): FocusSessionEntity?

    @Query("SELECT COALESCE(SUM(duration_minutes), 0) FROM focus_sessions WHERE end_time IS NOT NULL")
    fun getTotalFocusMinutes(): Flow<Int>
}
