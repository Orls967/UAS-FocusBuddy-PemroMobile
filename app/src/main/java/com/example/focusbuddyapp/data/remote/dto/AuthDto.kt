package com.example.focusbuddyapp.data.remote.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class RegisterRequestDto(
    val name: String,
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val major: String?,
    val avatarUrl: String?
)
