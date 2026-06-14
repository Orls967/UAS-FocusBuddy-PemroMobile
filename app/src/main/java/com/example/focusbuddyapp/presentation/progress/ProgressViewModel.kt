package com.example.focusbuddyapp.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProgressUiState(
    val weeklyData: Map<String, Int> = emptyMap(),   // "MON" -> minutes
    val totalFocusMinutes: Int = 0,
    val completedTaskCount: Int = 0,
    val focusSessionsCount: Int = 0,
    val bestDay: String = "Thursday",
    val consistencyPercent: Int = 0,
    val deepWorkHours: Float = 0f,
    val insightText: String = "Your focus peak usually happens mid-week. Harness this momentum!",
    val isLoading: Boolean = true
)

class ProgressViewModel : ViewModel() {
    private val getWeeklyFocusUseCase = AppModule.getWeeklyFocusDataUseCase
    private val taskRepository = AppModule.taskRepository
    private val focusSessionRepository = AppModule.focusSessionRepository

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init { loadProgress() }

    private fun loadProgress() = viewModelScope.launch {
        combine(
            getWeeklyFocusUseCase(),
            taskRepository.getAllTasks(),
            focusSessionRepository.getAllSessions()
        ) { weeklyData, tasks, sessions ->
            val total = weeklyData.values.sum()
            val bestDay = weeklyData.maxByOrNull { it.value }?.key ?: "—"
            val daysWithFocus = weeklyData.values.count { it > 0 }
            val consistency = if (weeklyData.isNotEmpty()) (daysWithFocus * 100 / 7) else 0
            val completedCount = tasks.count { it.isCompleted }
            val sessionCount = sessions.count { it.endTime != null }

            ProgressUiState(
                weeklyData = weeklyData,
                totalFocusMinutes = total,
                bestDay = bestDay,
                consistencyPercent = consistency,
                deepWorkHours = total / 60f,
                completedTaskCount = completedCount,
                focusSessionsCount = sessionCount,
                isLoading = false
            )
        }.collect { state ->
            _uiState.value = state
        }
    }
}

class ProgressViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProgressViewModel() as T
    }
}
