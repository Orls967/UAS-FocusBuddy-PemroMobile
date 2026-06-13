package com.example.focusbuddyapp.presentation.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.FocusSession
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TimerState { IDLE, RUNNING, PAUSED }

data class FocusUiState(
    val totalSeconds: Int = 30 * 60,        // Default 30 min
    val remainingSeconds: Int = 30 * 60,
    val timerState: TimerState = TimerState.IDLE,
    val breakDurationMinutes: Int = 5,
    val currentTaskTitle: String = "UTS Mobile",
    val todayFocusMinutes: Int = 0,
    val efficiencyPercent: Int = 85,
    val activeSessionId: Int? = null
) {
    val progress: Float get() = 1f - (remainingSeconds.toFloat() / totalSeconds)
    val timerText: String get() {
        val m = remainingSeconds / 60
        val s = remainingSeconds % 60
        return "%02d:%02d".format(m, s)
    }
    val modeLabel: String get() = "DEEP\nWORK"
}

class FocusViewModel : ViewModel() {
    private val startSessionUseCase = AppModule.startFocusSessionUseCase
    private val stopSessionUseCase = AppModule.stopFocusSessionUseCase
    private val getTodaySummaryUseCase = AppModule.getTodayFocusSummaryUseCase

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            getTodaySummaryUseCase().collect { minutes ->
                _uiState.update { it.copy(todayFocusMinutes = minutes) }
            }
        }
    }

    fun startTimer() = viewModelScope.launch {
        if (_uiState.value.timerState == TimerState.IDLE) {
            // Create a new session record
            val session = FocusSession(
                durationMinutes = _uiState.value.totalSeconds / 60,
                breakDurationMinutes = _uiState.value.breakDurationMinutes
            )
            val sessionId = startSessionUseCase(session)
            _uiState.update { it.copy(activeSessionId = sessionId.toInt()) }
        }
        _uiState.update { it.copy(timerState = TimerState.RUNNING) }
        runTimer()
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timerState = TimerState.PAUSED) }
    }

    fun stopTimer() = viewModelScope.launch {
        timerJob?.cancel()
        val state = _uiState.value
        state.activeSessionId?.let { id ->
            val session = FocusSession(
                id = id,
                durationMinutes = (state.totalSeconds - state.remainingSeconds) / 60,
                startTime = System.currentTimeMillis() - (state.totalSeconds - state.remainingSeconds) * 1000L
            )
            stopSessionUseCase(session)
        }
        _uiState.update { it.copy(timerState = TimerState.IDLE, remainingSeconds = it.totalSeconds, activeSessionId = null) }
    }

    fun setBreakDuration(minutes: Int) = _uiState.update { it.copy(breakDurationMinutes = minutes) }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.timerState == TimerState.RUNNING) {
                delay(1000L)
                _uiState.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            if (_uiState.value.remainingSeconds == 0) {
                stopTimer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

class FocusViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FocusViewModel() as T
    }
}
