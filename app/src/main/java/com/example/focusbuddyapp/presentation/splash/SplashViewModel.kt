package com.example.focusbuddyapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        _uiState.value = if (AppModule.authRepository.isLoggedIn()) {
            SplashUiState.NavigateToDashboard
        } else {
            SplashUiState.NavigateToLogin
        }
    }
}

class SplashViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SplashViewModel() as T
    }
}
