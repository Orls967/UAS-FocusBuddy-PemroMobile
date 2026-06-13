package com.example.focusbuddyapp.domain.usecase.analytics

import com.example.focusbuddyapp.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow

class GetWeeklyFocusDataUseCase(private val sessionRepository: FocusSessionRepository) {
    /** Returns a map of day abbreviation to minutes focused, e.g. {"MON" -> 45, "TUE" -> 120} */
    operator fun invoke(): Flow<Map<String, Int>> = sessionRepository.getWeeklyFocusMinutes()
}
