package com.example.focusbuddyapp.domain.usecase.auth

import com.example.focusbuddyapp.domain.model.User
import com.example.focusbuddyapp.domain.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        if (name.isBlank()) return Result.failure(Exception("Nama tidak boleh kosong"))
        if (email.isBlank()) return Result.failure(Exception("Email tidak boleh kosong"))
        if (password.isBlank()) return Result.failure(Exception("Password tidak boleh kosong"))
        if (password.length < 6) return Result.failure(Exception("Password minimal 6 karakter"))
        if (password != confirmPassword) return Result.failure(Exception("Konfirmasi password tidak sesuai"))
        return authRepository.register(name.trim(), email.trim(), password)
    }
}
