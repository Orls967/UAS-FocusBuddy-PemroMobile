package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository

class StopFocusSessionUseCase(private val repository: FocusSessionRepository) {
    suspend operator fun invoke(sessionId: Int, elapsedSeconds: Int, pomodoroMinutes: Int): Result<Unit> {
        return runCatching {
            val session = repository.getSessionById(sessionId) ?: throw Exception("Sesi tidak ditemukan")
            val elapsedMinutes = elapsedSeconds / 60
            val efficiency = calculateEfficiency(elapsedMinutes, pomodoroMinutes)
            val updated = session.copy(
                endTime = System.currentTimeMillis(),
                efficiencyPercent = efficiency
            )
            repository.updateSession(updated)
        }
    }

    private fun calculateEfficiency(elapsedMinutes: Int, pomodoroMinutes: Int): Int {
        if (pomodoroMinutes <= 0) return 100
        val ratio = (elapsedMinutes.toFloat() / pomodoroMinutes.toFloat()) * 100
        return ratio.toInt().coerceAtMost(100)
    }
}
