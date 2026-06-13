package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow

class GetTodayFocusSummaryUseCase(private val sessionRepository: FocusSessionRepository) {
    operator fun invoke(): Flow<Int> = sessionRepository.getTodayFocusMinutes()
}
