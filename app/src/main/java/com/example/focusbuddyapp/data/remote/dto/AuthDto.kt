package com.example.focusbuddyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("major") val major: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null
)

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class AuthResponseDto(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserDto
)
