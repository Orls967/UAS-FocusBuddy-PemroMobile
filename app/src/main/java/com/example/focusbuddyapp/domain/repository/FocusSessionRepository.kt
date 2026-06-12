package com.example.focusbuddyapp.domain.repository

import com.example.focusbuddyapp.domain.model.FocusSession
import kotlinx.coroutines.flow.Flow

interface FocusSessionRepository {
    suspend fun startSession(session: FocusSession): Long
    suspend fun updateSession(session: FocusSession)
    suspend fun getSessionById(id: Int): FocusSession?
    fun getTodaySessions(): Flow<List<FocusSession>>
    fun getTodayFocusMinutes(): Flow<Int?>
    fun getWeeklyFocusMinutes(): Flow<List<FocusSession>>
    fun getAllSessions(): Flow<List<FocusSession>>
    fun getSessionsForTask(taskId: Int): Flow<List<FocusSession>>
    suspend fun syncWithRemote()
}
