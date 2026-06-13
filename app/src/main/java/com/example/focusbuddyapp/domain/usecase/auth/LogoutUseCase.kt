package com.example.focusbuddyapp.domain.usecase.auth

import com.example.focusbuddyapp.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.logout()
}
