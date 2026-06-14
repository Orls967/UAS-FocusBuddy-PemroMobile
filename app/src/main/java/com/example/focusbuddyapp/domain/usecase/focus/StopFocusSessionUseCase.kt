package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository

class StopFocusSessionUseCase(private val sessionRepository: FocusSessionRepository) {
    suspend operator fun invoke(session: FocusSession) {
        val completedSession = session.copy(
            endTime = System.currentTimeMillis()
        )
        sessionRepository.updateSession(completedSession)
    }
}
