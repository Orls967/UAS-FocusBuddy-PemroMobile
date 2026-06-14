package com.example.focusbuddyapp.domain.repository

import com.example.focusbuddyapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
    fun isLoggedIn(): Boolean
    fun getAuthToken(): String?
    suspend fun updateProfile(userId: Int, name: String, avatarUrl: String?): Result<Unit>
}
