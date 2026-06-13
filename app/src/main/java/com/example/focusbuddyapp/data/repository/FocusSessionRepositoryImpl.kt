package com.example.focusbuddyapp.data.repository

import com.example.focusbuddyapp.data.local.dao.FocusSessionDao
import com.example.focusbuddyapp.data.mapper.toDomain
import com.example.focusbuddyapp.data.mapper.toEntity
import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar

class FocusSessionRepositoryImpl(
    private val focusSessionDao: FocusSessionDao
) : FocusSessionRepository {

    override suspend fun startSession(session: FocusSession): Long =
        withContext(Dispatchers.IO) { focusSessionDao.insertSession(session.toEntity()) }

    override suspend fun updateSession(session: FocusSession) =
        withContext(Dispatchers.IO) { focusSessionDao.updateSession(session.toEntity()) }

    override suspend fun getSessionById(id: Int): FocusSession? =
        withContext(Dispatchers.IO) { focusSessionDao.getSessionById(id)?.toDomain() }

    override fun getTodaySessions(): Flow<List<FocusSession>> {
        val (start, end) = todayBounds()
        return focusSessionDao.getTodaySessions(start, end).map { it.map { e -> e.toDomain() } }
    }

    override fun getTodayFocusMinutes(): Flow<Int> {
        val (start, end) = todayBounds()
        return focusSessionDao.getTodayFocusMinutes(start, end)
    }

    override fun getWeeklyFocusMinutes(): Flow<Map<String, Int>> {
        val weekStart = weekStartMillis()
        val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
        return focusSessionDao.getSessionsThisWeek(weekStart).map { sessions ->
            val map = mutableMapOf<String, Int>()
            days.forEach { map[it] = 0 }
            sessions.forEach { session ->
                val cal = Calendar.getInstance().apply { timeInMillis = session.startTime }
                val dayIndex = (cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7
                val dayKey = days.getOrNull(dayIndex) ?: return@forEach
                map[dayKey] = (map[dayKey] ?: 0) + session.durationMinutes
            }
            map.toMap()
        }
    }

    override fun getAllSessions(): Flow<List<FocusSession>> =
        focusSessionDao.getAllSessions().map { it.map { e -> e.toDomain() } }

    override fun getSessionsForTask(taskId: Int): Flow<List<FocusSession>> =
        focusSessionDao.getSessionsForTask(taskId).map { it.map { e -> e.toDomain() } }

    override suspend fun syncWithRemote() { /* TODO: implement remote session sync */ }

    private fun todayBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        return start to (start + 86_400_000L)
    }

    private fun weekStartMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
