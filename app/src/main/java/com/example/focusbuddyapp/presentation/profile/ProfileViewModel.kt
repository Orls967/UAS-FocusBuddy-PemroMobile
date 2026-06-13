package com.example.focusbuddyapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val pomodoroMinutes: Int = 25,
    val breakMinutes: Int = 5,
    val notificationsEnabled: Boolean = true,
    val totalFocusMinutes: Int = 0,
    val isLoading: Boolean = true
)

class ProfileViewModel : ViewModel() {
    private val logoutUseCase = AppModule.logoutUseCase
    private val userPreferences = AppModule.userPreferences
    private val authRepository = AppModule.authRepository
    private val focusSessionRepository = AppModule.focusSessionRepository

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadProfile() }

    private fun loadProfile() = viewModelScope.launch {
        combine(
            authRepository.getCurrentUser(),
            userPreferences.pomodoroMinutes,
            userPreferences.breakMinutes,
            userPreferences.notificationsEnabled,
            focusSessionRepository.getTodayFocusMinutes()
        ) { user, pomodoro, breakMin, notif, focusMin ->
            ProfileUiState(
                user = user,
                pomodoroMinutes = pomodoro,
                breakMinutes = breakMin,
                notificationsEnabled = notif,
                totalFocusMinutes = focusMin,
                isLoading = false
            )
        }.collect { _uiState.value = it }
    }

    fun setPomodoroMinutes(minutes: Int) = viewModelScope.launch {
        userPreferences.saveFocusSettings(minutes, _uiState.value.breakMinutes)
    }

    fun setBreakMinutes(minutes: Int) = viewModelScope.launch {
        userPreferences.saveFocusSettings(_uiState.value.pomodoroMinutes, minutes)
    }

    fun setNotificationsEnabled(enabled: Boolean) = viewModelScope.launch {
        userPreferences.saveNotificationEnabled(enabled)
    }

    fun logout() = viewModelScope.launch { logoutUseCase() }
}

class ProfileViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileViewModel() as T
    }
}
