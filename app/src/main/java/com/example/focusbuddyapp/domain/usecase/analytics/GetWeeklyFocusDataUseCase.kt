package com.example.focusbuddyapp.domain.usecase.analytics

import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow

class GetWeeklyFocusDataUseCase(private val repository: FocusSessionRepository) {
    operator fun invoke(): Flow<List<FocusSession>> = repository.getWeeklyFocusMinutes()
}
