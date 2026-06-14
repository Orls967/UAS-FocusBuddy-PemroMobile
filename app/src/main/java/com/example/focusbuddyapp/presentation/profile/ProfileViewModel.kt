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
    val isDarkMode: Boolean = false,
    val profileError: String? = null,
    val isSaving: Boolean = false
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
        val breakMin = when (minutes) {
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
            else -> minutes / 5
        }
        userPreferences.saveFocusSettings(minutes, breakMin)
    }

    fun setNotificationsEnabled(enabled: Boolean) = viewModelScope.launch {
        userPreferences.saveNotificationEnabled(enabled)
    }

    fun setDarkMode(enabled: Boolean) = viewModelScope.launch {
        userPreferences.saveDarkMode(enabled)
    }

    fun logout() = viewModelScope.launch { logoutUseCase() }

    fun updateProfile(name: String, photoUri: String?, onSuccess: () -> Unit) = viewModelScope.launch {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            _uiState.value = _uiState.value.copy(profileError = "Nama tidak boleh kosong")
            return@launch
        }
        
        val currentUser = _uiState.value.user ?: return@launch
        _uiState.value = _uiState.value.copy(isSaving = true)
        
        val result = authRepository.updateProfile(currentUser.id, trimmedName, photoUri)
        if (result.isSuccess) {
            _uiState.value = _uiState.value.copy(profileError = null, isSaving = false)
            onSuccess()
        } else {
            _uiState.value = _uiState.value.copy(
                profileError = result.exceptionOrNull()?.message ?: "Gagal memperbarui profil",
                isSaving = false
            )
        }
    }

    fun clearProfileError() {
        _uiState.value = _uiState.value.copy(profileError = null)
    }
}

class ProfileViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileViewModel() as T
    }
}
