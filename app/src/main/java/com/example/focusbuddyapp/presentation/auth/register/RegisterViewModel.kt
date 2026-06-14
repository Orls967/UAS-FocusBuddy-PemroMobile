package com.example.focusbuddyapp.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isPrivacyPolicyViewed: Boolean = false,
    val isPrivacyPolicyAccepted: Boolean = false,
    val showPrivacyPolicyDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel : ViewModel() {
    private val registerUseCase = AppModule.registerUseCase

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, errorMessage = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, errorMessage = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, errorMessage = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, errorMessage = null) }
    fun togglePasswordVisibility() = _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    
    fun onPrivacyPolicyAcceptChange(v: Boolean) {
        if (_uiState.value.isPrivacyPolicyViewed) {
            _uiState.update { it.copy(isPrivacyPolicyAccepted = v, errorMessage = null) }
        }
    }
    
    fun showPrivacyPolicyDialog() = _uiState.update { it.copy(showPrivacyPolicyDialog = true, errorMessage = null) }
    fun hidePrivacyPolicyDialog() = _uiState.update { it.copy(showPrivacyPolicyDialog = false) }
    
    fun onPrivacyPolicyUnderstood() = _uiState.update { 
        it.copy(
            showPrivacyPolicyDialog = false, 
            isPrivacyPolicyViewed = true,
            errorMessage = null
        )
    }

    fun register() = viewModelScope.launch {
        val s = _uiState.value
        if (!s.isPrivacyPolicyViewed) {
            _uiState.update { it.copy(errorMessage = "Please review the Privacy Policy before creating an account.") }
            return@launch
        }
        if (!s.isPrivacyPolicyAccepted) {
            _uiState.update { it.copy(errorMessage = "You must agree to the Privacy Policy before creating an account.") }
            return@launch
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = registerUseCase(s.name, s.email, s.password, s.confirmPassword)
        result.fold(
            onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
            onFailure = { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        )
    }
}

class RegisterViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RegisterViewModel() as T
    }
}
