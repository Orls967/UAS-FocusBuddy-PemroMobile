package com.example.focusbuddyapp.data.repository

import com.example.focusbuddyapp.data.local.dao.FocusSessionDao
import com.example.focusbuddyapp.data.local.entity.FocusSessionEntity
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

    override suspend fun startSession(session: FocusSession): Long = withContext(Dispatchers.IO) {
        focusSessionDao.insertSession(session.toEntity())
    }

    override suspend fun updateSession(session: FocusSession) = withContext(Dispatchers.IO) {
        focusSessionDao.updateSession(session.toEntity())
    }

    override suspend fun getSessionById(id: Int): FocusSession? = withContext(Dispatchers.IO) {
        focusSessionDao.getSessionById(id)?.toDomain()
    }

    override fun getTodaySessions(): Flow<List<FocusSession>> {
        val start = todayStart()
        return focusSessionDao.getTodaySessions(start).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTodayFocusMinutes(): Flow<Int?> {
        val start = todayStart()
        return focusSessionDao.getTodayFocusMinutes(start)
    }

    override fun getWeeklyFocusMinutes(): Flow<List<FocusSession>> {
        val start = weekStartMillis()
        return focusSessionDao.getSessionsFromDate(start).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllSessions(): Flow<List<FocusSession>> =
        focusSessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getSessionsForTask(taskId: Int): Flow<List<FocusSession>> =
        focusSessionDao.getSessionsForTask(taskId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun syncWithRemote() {}

    private fun todayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun weekStartMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
