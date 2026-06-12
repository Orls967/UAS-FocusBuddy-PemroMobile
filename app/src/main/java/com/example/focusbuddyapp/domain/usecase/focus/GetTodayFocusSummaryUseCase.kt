package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow

class GetTodayFocusSummaryUseCase(private val repository: FocusSessionRepository) {
    fun getMinutes(): Flow<Int?> = repository.getTodayFocusMinutes()
}
