package com.example.focusbuddyapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface SplashUiState {
    object Loading : SplashUiState
    object NavigateToLogin : SplashUiState
    object NavigateToDashboard : SplashUiState
}

class SplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() = viewModelScope.launch {
        delay(1800) // Show splash for at least 1.8s
        
        val token = AppModule.authRepository.getAuthToken()
        val userId = try {
            AppModule.userPreferences.userId.first()
        } catch (e: Exception) {
            0
        }
        
        val isValid = if (!token.isNullOrEmpty() && userId > 0) {
            val user = AppModule.userDao.getUserById(userId)
            user != null
        } else {
            false
        }
        
        if (isValid) {
            _uiState.value = SplashUiState.NavigateToDashboard
        } else {
            AppModule.userPreferences.clearAuthSession()
            _uiState.value = SplashUiState.NavigateToLogin
        }
    }
}

class SplashViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SplashViewModel() as T
    }
}
