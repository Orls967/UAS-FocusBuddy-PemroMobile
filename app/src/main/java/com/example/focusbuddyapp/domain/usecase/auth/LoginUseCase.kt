package com.example.focusbuddyapp.domain.usecase.auth

import com.example.focusbuddyapp.domain.model.User
import com.example.focusbuddyapp.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.failure(Exception("Email tidak boleh kosong"))
        if (password.isBlank()) return Result.failure(Exception("Password tidak boleh kosong"))
        if (password.length < 6) return Result.failure(Exception("Password minimal 6 karakter"))
        return authRepository.login(email.trim(), password)
    }
}
