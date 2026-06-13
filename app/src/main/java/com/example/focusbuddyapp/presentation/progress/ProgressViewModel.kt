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
    val bestDay: String = "Thursday",
    val consistencyPercent: Int = 0,
    val deepWorkHours: Float = 0f,
    val rank: String = "TOP 5",
    val insightText: String = "Your focus peak usually happens mid-week. Harness this momentum!",
    val isLoading: Boolean = true
)

class ProgressViewModel : ViewModel() {
    private val getWeeklyFocusUseCase = AppModule.getWeeklyFocusDataUseCase

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init { loadProgress() }

    private fun loadProgress() = viewModelScope.launch {
        getWeeklyFocusUseCase().collect { weeklyData ->
            val total = weeklyData.values.sum()
            val bestDay = weeklyData.maxByOrNull { it.value }?.key ?: "—"
            val daysWithFocus = weeklyData.values.count { it > 0 }
            val consistency = if (weeklyData.isNotEmpty()) (daysWithFocus * 100 / 7) else 0

            _uiState.update {
                it.copy(
                    weeklyData = weeklyData,
                    totalFocusMinutes = total,
                    bestDay = bestDay,
                    consistencyPercent = consistency,
                    deepWorkHours = total / 60f,
                    isLoading = false
                )
            }
        }
    }
}

class ProgressViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProgressViewModel() as T
    }
}
