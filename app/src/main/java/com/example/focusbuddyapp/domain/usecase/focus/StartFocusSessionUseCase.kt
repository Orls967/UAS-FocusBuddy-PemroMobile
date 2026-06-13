package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository

class StartFocusSessionUseCase(private val sessionRepository: FocusSessionRepository) {
    suspend operator fun invoke(session: FocusSession): Long =
        sessionRepository.startSession(session)
}
