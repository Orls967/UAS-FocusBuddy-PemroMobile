package com.example.focusbuddyapp.data.remote.api

import com.example.focusbuddyapp.data.remote.dto.*
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("auth/logout")
    suspend fun logout()
}
