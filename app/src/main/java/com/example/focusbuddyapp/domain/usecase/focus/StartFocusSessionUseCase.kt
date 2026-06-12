package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository

class StartFocusSessionUseCase(private val repository: FocusSessionRepository) {
    suspend operator fun invoke(linkedTaskId: Int?, durationMinutes: Int): Result<Long> {
        val session = FocusSession(
            linkedTaskId = linkedTaskId,
            durationMinutes = durationMinutes,
            startTime = System.currentTimeMillis()
        )
        return runCatching { repository.startSession(session) }
    }
}
