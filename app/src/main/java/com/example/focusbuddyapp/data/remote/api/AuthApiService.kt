package com.example.focusbuddyapp.data.remote.api

import com.example.focusbuddyapp.data.remote.dto.LoginRequestDto
import com.example.focusbuddyapp.data.remote.dto.LoginResponseDto
import com.example.focusbuddyapp.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): LoginResponseDto

    @POST("auth/logout")
    suspend fun logout()
}
