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
    val isLoading: Boolean = true,
    val profilePhotoUri: String? = null,
    val isDarkMode: Boolean = false
)

private data class ProfilePrefsData(
    val pomodoro: Int,
    val breakMin: Int,
    val notif: Boolean,
    val dark: Boolean
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
        val prefsFlow = combine(
            userPreferences.pomodoroMinutes,
            userPreferences.breakMinutes,
            userPreferences.notificationsEnabled,
            userPreferences.isDarkMode
        ) { pomodoro, breakMin, notif, dark ->
            ProfilePrefsData(pomodoro, breakMin, notif, dark)
        }

        val baseFlow = combine(
            authRepository.getCurrentUser(),
            prefsFlow,
            focusSessionRepository.getTodayFocusMinutes()
        ) { user, prefs, focusMin ->
            ProfileUiState(
                user = user,
                pomodoroMinutes = prefs.pomodoro,
                breakMinutes = prefs.breakMin,
                notificationsEnabled = prefs.notif,
                isDarkMode = prefs.dark,
                totalFocusMinutes = focusMin,
                isLoading = false
            )
        }

        combine(baseFlow, userPreferences.profilePhotoUri) { state, photoUri ->
            state.copy(profilePhotoUri = photoUri)
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

    fun setDarkMode(enabled: Boolean) = viewModelScope.launch {
        userPreferences.saveDarkMode(enabled)
    }

    fun logout() = viewModelScope.launch { logoutUseCase() }

    fun setProfilePhoto(uri: String?) = viewModelScope.launch {
        userPreferences.saveProfilePhotoUri(uri)
    }
}

class ProfileViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileViewModel() as T
    }
}
