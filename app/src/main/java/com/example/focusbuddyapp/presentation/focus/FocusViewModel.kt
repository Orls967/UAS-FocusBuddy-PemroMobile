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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TimerState { IDLE, RUNNING, PAUSED }

data class FocusUiState(
    val totalSeconds: Int = 30 * 60,        // Default 30 min
    val remainingSeconds: Int = 30 * 60,
    val timerState: TimerState = TimerState.IDLE,
    val breakDurationMinutes: Int = 5,
    val currentTaskTitle: String = "No Active Task",
    val todayFocusMinutes: Int = 0,
    val efficiencyPercent: Int = 85,
    val activeSessionId: Int? = null,
    val profilePhotoUri: String? = null,
    val isBreakMode: Boolean = false,
    val showBreakDialog: Boolean = false
) {
    val progress: Float get() = 1f - (remainingSeconds.toFloat() / totalSeconds)
    val timerText: String get() {
        val m = remainingSeconds / 60
        val s = remainingSeconds % 60
        return "%02d:%02d".format(m, s)
    }
    val modeLabel: String get() = if (isBreakMode) "BREAK\nTIME" else "DEEP\nWORK"
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
        viewModelScope.launch {
            AppModule.userPreferences.activeTaskTitle.collect { title ->
                _uiState.update { it.copy(currentTaskTitle = title.ifBlank { "No Active Task" }) }
            }
        }
        viewModelScope.launch {
            AppModule.userPreferences.profilePhotoUri.collect { uri ->
                _uiState.update { it.copy(profilePhotoUri = uri) }
            }
        }
        viewModelScope.launch {
            AppModule.userPreferences.pomodoroMinutes.collect { minutes ->
                if (_uiState.value.timerState == TimerState.IDLE) {
                    val totalSecs = minutes * 60
                    _uiState.update {
                        it.copy(
                            totalSeconds = totalSecs,
                            remainingSeconds = totalSecs
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            AppModule.userPreferences.breakMinutes.collect { minutes ->
                _uiState.update { it.copy(breakDurationMinutes = minutes) }
            }
        }
    }

    fun startTimer() = viewModelScope.launch {
        if (_uiState.value.timerState == TimerState.IDLE) {
            val activeTaskId = AppModule.userPreferences.activeTaskId.first()
            // Create a new session record
            val session = FocusSession(
                durationMinutes = _uiState.value.totalSeconds / 60,
                breakDurationMinutes = _uiState.value.breakDurationMinutes,
                linkedTaskId = if (activeTaskId != 0) activeTaskId else null
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
        stopSessionAndReset(showDialogAfter = false)
    }

    fun startBreak() {
        timerJob?.cancel()
        val totalSecs = _uiState.value.breakDurationMinutes * 60
        _uiState.update {
            it.copy(
                isBreakMode = true,
                showBreakDialog = false,
                totalSeconds = totalSecs,
                remainingSeconds = totalSecs,
                timerState = TimerState.RUNNING
            )
        }
        runTimer()
    }

    fun skipBreak() {
        resetToIdle()
    }

    fun dismissBreakDialog() {
        _uiState.update { it.copy(showBreakDialog = false) }
    }

    private fun resetToIdle() = viewModelScope.launch {
        timerJob?.cancel()
        val pomodoroMinutes = AppModule.userPreferences.pomodoroMinutes.first()
        val totalSecs = pomodoroMinutes * 60
        _uiState.update {
            it.copy(
                isBreakMode = false,
                showBreakDialog = false,
                timerState = TimerState.IDLE,
                totalSeconds = totalSecs,
                remainingSeconds = totalSecs,
                activeSessionId = null
            )
        }
    }

    private suspend fun stopSessionAndReset(showDialogAfter: Boolean) {
        timerJob?.cancel()
        val state = _uiState.value
        if (!state.isBreakMode) {
            state.activeSessionId?.let { id ->
                val session = FocusSession(
                    id = id,
                    durationMinutes = (state.totalSeconds - state.remainingSeconds) / 60,
                    startTime = System.currentTimeMillis() - (state.totalSeconds - state.remainingSeconds) * 1000L
                )
                stopSessionUseCase(session)
            }
        }
        val pomodoroMinutes = AppModule.userPreferences.pomodoroMinutes.first()
        val totalSecs = pomodoroMinutes * 60
        _uiState.update {
            it.copy(
                timerState = TimerState.IDLE,
                isBreakMode = false,
                showBreakDialog = showDialogAfter,
                remainingSeconds = totalSecs,
                totalSeconds = totalSecs,
                activeSessionId = null
            )
        }
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
                if (!_uiState.value.isBreakMode) {
                    stopSessionAndReset(showDialogAfter = true)
                } else {
                    resetToIdle()
                }
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
