package com.example.focusbuddyapp.presentation.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.FocusSession
import com.example.focusbuddyapp.domain.model.Task
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
    val activeSessionId: Int? = null,
    val profilePhotoUri: String? = null,
    val isBreakMode: Boolean = false,
    val showBreakDialog: Boolean = false,
    val showNextTaskModal: Boolean = false,
    val showCompleteTaskDialog: Boolean = false,
    val hasActiveTask: Boolean = false,
    val activeTasks: List<Task> = emptyList()
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
            AppModule.userPreferences.breakMinutes.collect { breakMin ->
                _uiState.update { it.copy(breakDurationMinutes = breakMin) }
            }
        }
        viewModelScope.launch {
            AppModule.userPreferences.activeTaskId.collect { activeId ->
                val hasTask = activeId != 0
                val focusMin = if (hasTask) {
                    AppModule.taskRepository.getTaskById(activeId)?.focusDuration ?: 25
                } else {
                    25
                }
                
                _uiState.update { it.copy(hasActiveTask = hasTask) }
                
                if (_uiState.value.timerState == TimerState.IDLE) {
                    val totalSecs = focusMin * 60
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
            AppModule.taskRepository.getAllTasks().collect { tasks ->
                val active = tasks.filter { !it.isCompleted }
                _uiState.update { it.copy(activeTasks = active) }
            }
        }
    }

    fun getBreakDurationForFocus(focusMinutes: Int): Int {
        return when (focusMinutes) {
            15 -> 3
            20 -> 4
            25 -> 5
            30 -> 6
            35 -> 7
            40 -> 8
            45 -> 9
            50 -> 10
            55 -> 11
            60 -> 12
            65 -> 13
            70 -> 14
            75 -> 15
            else -> focusMinutes / 5
        }
    }

    fun startTimer() = viewModelScope.launch {
        if (_uiState.value.timerState == TimerState.IDLE) {
            val activeTaskId = AppModule.userPreferences.activeTaskId.first()
            val session = FocusSession(
                durationMinutes = _uiState.value.totalSeconds / 60,
                breakDurationMinutes = _uiState.value.breakDurationMinutes,
                linkedTaskId = if (activeTaskId != 0) activeTaskId else null
            )
            val sessionId = startSessionUseCase(session)
            _uiState.update { it.copy(activeSessionId = sessionId.toInt()) }
        }
        AppModule.userPreferences.saveFocusLocked(true)
        _uiState.update { it.copy(timerState = TimerState.RUNNING) }
        runTimer()
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timerState = TimerState.PAUSED) }
    }

    fun stopTimer() = viewModelScope.launch {
        stopSessionAndReset(showDialogAfter = false)
        AppModule.userPreferences.saveFocusLocked(false)
    }

    fun completeTask() = viewModelScope.launch {
        val activeTaskId = AppModule.userPreferences.activeTaskId.first()
        if (activeTaskId != 0) {
            AppModule.toggleTaskCompleteUseCase(activeTaskId, true)
            AppModule.userPreferences.clearActiveTask()
        }
        stopSessionAndReset(showDialogAfter = false)
        AppModule.userPreferences.saveFocusLocked(false)
        dismissCompleteTaskDialog()
    }

    fun showCompleteTaskDialog() {
        _uiState.update { it.copy(showCompleteTaskDialog = true) }
    }

    fun dismissCompleteTaskDialog() {
        _uiState.update { it.copy(showCompleteTaskDialog = false) }
    }

    fun startBreakAuto() = viewModelScope.launch {
        timerJob?.cancel()
        saveCompletedSession()
        
        val breakMin = AppModule.userPreferences.breakMinutes.first()
        val totalSecs = breakMin * 60
        _uiState.update {
            it.copy(
                isBreakMode = true,
                showBreakDialog = false,
                totalSeconds = totalSecs,
                remainingSeconds = totalSecs,
                timerState = TimerState.RUNNING,
                activeSessionId = null
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
        AppModule.userPreferences.saveFocusLocked(false)
        val activeId = AppModule.userPreferences.activeTaskId.first()
        val hasTask = activeId != 0
        val focusMin = if (hasTask) {
            AppModule.taskRepository.getTaskById(activeId)?.focusDuration ?: 25
        } else {
            25
        }
        val totalSecs = focusMin * 60
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

    private suspend fun saveCompletedSession() {
        val state = _uiState.value
        state.activeSessionId?.let { id ->
            val session = FocusSession(
                id = id,
                durationMinutes = state.totalSeconds / 60,
                startTime = System.currentTimeMillis() - state.totalSeconds * 1000L
            )
            stopSessionUseCase(session)
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
        val activeId = AppModule.userPreferences.activeTaskId.first()
        val hasTask = activeId != 0
        val focusMin = if (hasTask) {
            AppModule.taskRepository.getTaskById(activeId)?.focusDuration ?: 25
        } else {
            25
        }
        val totalSecs = focusMin * 60
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

    fun showNextTaskModal() {
        _uiState.update { it.copy(showNextTaskModal = true) }
    }

    fun dismissNextTaskModal() {
        _uiState.update { it.copy(showNextTaskModal = false) }
    }

    fun selectNextTaskAndStopBreak(task: Task) = viewModelScope.launch {
        timerJob?.cancel()
        AppModule.userPreferences.saveFocusLocked(false)
        
        AppModule.userPreferences.saveActiveTask(task.id, task.title)
        
        val focusMin = task.focusDuration
        val totalSecs = focusMin * 60
        _uiState.update {
            it.copy(
                isBreakMode = false,
                showNextTaskModal = false,
                totalSeconds = totalSecs,
                remainingSeconds = totalSecs,
                timerState = TimerState.IDLE,
                currentTaskTitle = task.title,
                activeSessionId = null,
                hasActiveTask = true
            )
        }
    }

    private fun runTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.timerState == TimerState.RUNNING) {
                delay(1000L)
                _uiState.update { it.copy(remainingSeconds = it.remainingSeconds - 1) }
            }
            if (_uiState.value.remainingSeconds == 0) {
                if (!_uiState.value.isBreakMode) {
                    val activeTaskId = AppModule.userPreferences.activeTaskId.first()
                    if (activeTaskId != 0) {
                        val activeTask = AppModule.taskRepository.getTaskById(activeTaskId)
                        if (activeTask != null && !activeTask.isCompleted) {
                            AppModule.toggleTaskCompleteUseCase(activeTaskId, true)
                        }
                    }
                    
                    val notifEnabled = AppModule.userPreferences.notificationsEnabled.first()
                    if (notifEnabled) {
                        com.example.focusbuddyapp.ui.util.NotificationHelper.showSessionCompleteNotification(
                            AppModule.appContext,
                            _uiState.value.currentTaskTitle
                        )
                    }
                    
                    startBreakAuto()
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
