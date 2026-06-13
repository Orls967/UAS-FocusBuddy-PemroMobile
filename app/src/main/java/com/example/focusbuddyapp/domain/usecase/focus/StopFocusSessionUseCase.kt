package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository

class StopFocusSessionUseCase(private val sessionRepository: FocusSessionRepository) {
    suspend operator fun invoke(session: FocusSession) {
        val completedSession = session.copy(
            endTime = System.currentTimeMillis(),
            efficiencyPercent = calculateEfficiency(session)
        )
        sessionRepository.updateSession(completedSession)
    }

    private fun calculateEfficiency(session: FocusSession): Int {
        val planned = session.durationMinutes * 60 * 1000L
        val elapsed = System.currentTimeMillis() - session.startTime
        return if (elapsed >= planned) 100 else ((elapsed.toFloat() / planned) * 100).toInt()
    }
}
