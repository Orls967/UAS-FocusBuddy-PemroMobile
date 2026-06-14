package com.example.focusbuddyapp.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProgressUiState(
    val weeklyData: Map<String, Int> = emptyMap(),   // "MON" -> minutes
    val tasksByDay: Map<String, List<Task>> = emptyMap(),
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
            val totalAllTime = tasks.filter { it.isCompleted }.sumOf { it.focusDuration }
            val bestDay = weeklyData.maxByOrNull { it.value }?.key ?: "—"
            val daysWithFocus = weeklyData.values.count { it > 0 }
            val consistency = if (weeklyData.isNotEmpty()) (daysWithFocus * 100 / 7) else 0
            val completedCount = tasks.count { it.isCompleted }
            val actualSessionsCount = sessions.size

            val cal = java.util.Calendar.getInstance()
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
            cal.set(java.util.Calendar.MINUTE, 0)
            cal.set(java.util.Calendar.SECOND, 0)
            cal.set(java.util.Calendar.MILLISECOND, 0)
            val offset = when (cal.get(java.util.Calendar.DAY_OF_WEEK)) {
                java.util.Calendar.SUNDAY -> -6
                java.util.Calendar.MONDAY -> 0
                java.util.Calendar.TUESDAY -> -1
                java.util.Calendar.WEDNESDAY -> -2
                java.util.Calendar.THURSDAY -> -3
                java.util.Calendar.FRIDAY -> -4
                java.util.Calendar.SATURDAY -> -5
                else -> 0
            }
            cal.add(java.util.Calendar.DAY_OF_MONTH, offset)
            val startOfWeek = cal.timeInMillis
            val endOfWeek = startOfWeek + 7 * 24 * 60 * 60 * 1000L

            val format = java.text.SimpleDateFormat("EEE", java.util.Locale.ENGLISH)

            val tasksByDay = tasks.filter {
                it.isCompleted && it.completedAt != null &&
                it.completedAt >= startOfWeek && it.completedAt < endOfWeek
            }.groupBy { task ->
                val dayCal = java.util.Calendar.getInstance().apply { timeInMillis = task.completedAt!! }
                format.format(dayCal.time).uppercase(java.util.Locale.ENGLISH)
            }

            ProgressUiState(
                weeklyData = weeklyData,
                tasksByDay = tasksByDay,
                totalFocusMinutes = totalAllTime,
                bestDay = bestDay,
                consistencyPercent = consistency,
                deepWorkHours = totalAllTime / 60f,
                completedTaskCount = completedCount,
                focusSessionsCount = actualSessionsCount,
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
