package com.example.focusbuddyapp.data.repository

import com.example.focusbuddyapp.data.local.dao.UserDao
import com.example.focusbuddyapp.data.mapper.toDomain
import com.example.focusbuddyapp.data.mapper.toEntity
import com.example.focusbuddyapp.data.preferences.UserPreferences
import com.example.focusbuddyapp.data.remote.api.AuthApiService
import com.example.focusbuddyapp.domain.model.User
import com.example.focusbuddyapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authApiService: AuthApiService, // Keep to avoid breaking AppModule/DI
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) : AuthRepository {

    private suspend fun seedUsersIfEmpty() {
        if (userDao.getUserByEmail("user1@example.com") == null) {
            userDao.insertUser(
                com.example.focusbuddyapp.data.local.entity.UserEntity(
                    name = "Scholar One",
                    email = "user1@example.com",
                    password = "password",
                    major = "Computer Science"
                )
            )
        }
        if (userDao.getUserByEmail("user2@example.com") == null) {
            userDao.insertUser(
                com.example.focusbuddyapp.data.local.entity.UserEntity(
                    name = "Scholar Two",
                    email = "user2@example.com",
                    password = "password",
                    major = "Information Systems"
                )
            )
        }
        if (userDao.getUserByEmail("user3@example.com") == null) {
            userDao.insertUser(
                com.example.focusbuddyapp.data.local.entity.UserEntity(
                    name = "Scholar Three",
                    email = "user3@example.com",
                    password = "password",
                    major = "Information Technology"
                )
            )
        }
    }

    override suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                seedUsersIfEmpty()
                val normalizedEmail = email.trim().lowercase()
                val userEntity = userDao.getUserByEmail(normalizedEmail)
                    ?: throw Exception("Email tidak terdaftar")
                if (userEntity.password != password) {
                    throw Exception("Password salah")
                }
                val token = "local-token-for-${userEntity.id}"
                userPreferences.saveLoginSession(token, userEntity.id, userEntity.name, userEntity.email)
                userEntity.toDomain()
            }
        }

    override suspend fun register(name: String, email: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                seedUsersIfEmpty()
                val normalizedEmail = email.trim().lowercase()
                val existing = userDao.getUserByEmail(normalizedEmail)
                if (existing != null) {
                    throw Exception("Email sudah terdaftar")
                }
                val userEntity = com.example.focusbuddyapp.data.local.entity.UserEntity(
                    name = name.trim(),
                    email = normalizedEmail,
                    password = password
                )
                val newId = userDao.insertUser(userEntity)
                val finalUser = userEntity.copy(id = newId.toInt()).toDomain()
                
                val token = "local-token-for-${newId}"
                userPreferences.saveLoginSession(token, finalUser.id, finalUser.name, finalUser.email)
                finalUser
            }
        }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        userPreferences.clearAll()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentUser(): Flow<User?> =
        userPreferences.userId.flatMapLatest { id ->
            if (id == 0) {
                flowOf(null)
            } else {
                userDao.getUserByIdFlow(id).map { it?.toDomain() }
            }
        }

    override fun isLoggedIn(): Boolean =
        userPreferences.getAuthTokenBlocking() != null

    override fun getAuthToken(): String? =
        userPreferences.getAuthTokenBlocking()
}
