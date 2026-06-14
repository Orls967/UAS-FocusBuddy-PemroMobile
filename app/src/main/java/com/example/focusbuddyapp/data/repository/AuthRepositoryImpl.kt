package com.example.focusbuddyapp.data.repository

import com.example.focusbuddyapp.data.local.dao.UserDao
import com.example.focusbuddyapp.data.local.dao.TaskDao
import com.example.focusbuddyapp.data.local.dao.FocusSessionDao
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authApiService: AuthApiService, // Keep to avoid breaking AppModule/DI
    private val userDao: UserDao,
    private val userPreferences: UserPreferences,
    private val taskDao: TaskDao,
    private val focusSessionDao: FocusSessionDao
) : AuthRepository {

    private suspend fun seedUsersIfEmpty() {
        if (userDao.getUserByEmail("demo@focusbuddy.com") == null) {
            userDao.insertUser(
                com.example.focusbuddyapp.data.local.entity.UserEntity(
                    name = "Demo Scholar",
                    email = "demo@focusbuddy.com",
                    password = "password",
                    major = "Computer Science"
                )
            )
        }
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

    private suspend fun seedDemoData(userId: Int) {
        val activeTasks = listOf(
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Review Materi Mobile Programming",
                description = "Membaca kembali modul perkuliahan bab 1-8",
                category = "Kuliah",
                priority = "HIGH",
                userId = userId,
                isCompleted = false
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Persiapan Presentasi UAS",
                description = "Menyiapkan slide dan materi presentasi FocusBuddy",
                category = "Presentasi",
                priority = "HIGH",
                userId = userId,
                isCompleted = false
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Finalisasi Dokumentasi Project",
                description = "Menyusun laporan akhir dan readme file GitHub",
                category = "Laporan",
                priority = "MEDIUM",
                userId = userId,
                isCompleted = false
            )
        )

        val completedTodayTasks = listOf(
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Implementasi Dark Mode",
                description = "Menerapkan visual contrast di seluruh halaman",
                category = "Coding",
                priority = "HIGH",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Refactor Repository Layer",
                description = "Membersihkan DAO dan mengoptimalkan query Room",
                category = "Coding",
                priority = "HIGH",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Testing Notification Feature",
                description = "Menguji local notification ketika timer selesai",
                category = "Testing",
                priority = "MEDIUM",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Perbaikan Profile UI",
                description = "Memperbaiki layout dialog dan text cut-off",
                category = "UI/UX",
                priority = "MEDIUM",
                userId = userId,
                isCompleted = true
            )
        )

        val historicalTasks = listOf(
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Analisis Kebutuhan Pengguna",
                description = "Melakukan wawancara singkat untuk fitur FocusBuddy",
                category = "Riset",
                priority = "LOW",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Perancangan Arsitektur MVVM",
                description = "Membuat diagram navigasi dan struktur package",
                category = "Desain",
                priority = "MEDIUM",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Pembuatan Unit Testing",
                description = "Menulis unit test untuk use case dan viewmodel",
                category = "Testing",
                priority = "HIGH",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Integrasi Room Database",
                description = "Membuat entity, dao, dan konfigurasi database",
                category = "Coding",
                priority = "HIGH",
                userId = userId,
                isCompleted = true
            ),
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Desain Wireframe & Mockup",
                description = "Mendesain UI Figma untuk halaman utama dan detail",
                category = "UI/UX",
                priority = "MEDIUM",
                userId = userId,
                isCompleted = true
            )
        )

        val activeIds = activeTasks.map { taskDao.insertTask(it).toInt() }
        val completedTodayIds = completedTodayTasks.map { taskDao.insertTask(it).toInt() }
        val historicalIds = historicalTasks.map { taskDao.insertTask(it).toInt() }

        val now = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000L

        val sessions = listOf(
            // Hari Ini (Today) - 4 sessions
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = completedTodayIds[0], // Implementasi Dark Mode
                durationMinutes = 25,
                startTime = now - 6 * 60 * 60 * 1000L - 25 * 60 * 1000L,
                endTime = now - 6 * 60 * 60 * 1000L
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = completedTodayIds[1], // Refactor Repository Layer
                durationMinutes = 25,
                startTime = now - 4 * 60 * 60 * 1000L - 25 * 60 * 1000L,
                endTime = now - 4 * 60 * 60 * 1000L
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = completedTodayIds[2], // Testing Notification Feature
                durationMinutes = 50,
                startTime = now - 2 * 60 * 60 * 1000L - 50 * 60 * 1000L,
                endTime = now - 2 * 60 * 60 * 1000L
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = completedTodayIds[3], // Perbaikan Profile UI
                durationMinutes = 25,
                startTime = now - 30 * 60 * 1000L - 25 * 60 * 1000L,
                endTime = now - 30 * 60 * 1000L
            ),
            // Kemarin (Yesterday) - 3 sessions
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[0],
                durationMinutes = 25,
                startTime = now - oneDay - 25 * 60 * 1000L,
                endTime = now - oneDay
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[1],
                durationMinutes = 25,
                startTime = now - oneDay - 2 * 60 * 60 * 1000L - 25 * 60 * 1000L,
                endTime = now - oneDay - 2 * 60 * 60 * 1000L
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[2],
                durationMinutes = 50,
                startTime = now - oneDay - 4 * 60 * 60 * 1000L - 50 * 60 * 1000L,
                endTime = now - oneDay - 4 * 60 * 60 * 1000L
            ),
            // 2 Hari Lalu - 2 sessions
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[3],
                durationMinutes = 25,
                startTime = now - 2 * oneDay - 25 * 60 * 1000L,
                endTime = now - 2 * oneDay
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[4],
                durationMinutes = 25,
                startTime = now - 2 * oneDay - 2 * 60 * 60 * 1000L - 25 * 60 * 1000L,
                endTime = now - 2 * oneDay - 2 * 60 * 60 * 1000L
            ),
            // 3 Hari Lalu - 2 sessions
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[0],
                durationMinutes = 50,
                startTime = now - 3 * oneDay - 50 * 60 * 1000L,
                endTime = now - 3 * oneDay
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[1],
                durationMinutes = 25,
                startTime = now - 3 * oneDay - 2 * 60 * 60 * 1000L - 25 * 60 * 1000L,
                endTime = now - 3 * oneDay - 2 * 60 * 60 * 1000L
            ),
            // 4 Hari Lalu - 2 sessions
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[2],
                durationMinutes = 25,
                startTime = now - 4 * oneDay - 25 * 60 * 1000L,
                endTime = now - 4 * oneDay
            ),
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[3],
                durationMinutes = 30,
                startTime = now - 4 * oneDay - 3 * 60 * 60 * 1000L - 30 * 60 * 1000L,
                endTime = now - 4 * oneDay - 3 * 60 * 60 * 1000L
            ),
            // 5 Hari Lalu - 1 session
            com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                linkedTaskId = historicalIds[4],
                durationMinutes = 45,
                startTime = now - 5 * oneDay - 45 * 60 * 1000L,
                endTime = now - 5 * oneDay
            )
        )

        sessions.forEach {
            focusSessionDao.insertSession(it)
        }

        completedTodayIds.forEach { taskDao.toggleTaskComplete(it, true) }
        historicalIds.forEach { taskDao.toggleTaskComplete(it, true) }
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
                
                if (normalizedEmail == "demo@focusbuddy.com") {
                    val taskCount = taskDao.getTaskCountForUser(userEntity.id)
                    if (taskCount == 0) {
                        seedDemoData(userEntity.id)
                        userPreferences.setDemoDataSeeded(true)
                    }
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
                if (normalizedEmail == "demo@focusbuddy.com") {
                    throw Exception("Email ini adalah akun demo bawaan")
                }
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
        userPreferences.clearAuthSession()
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

    override suspend fun updateProfile(userId: Int, name: String, avatarUrl: String?): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val existingUser = userDao.getUserById(userId) ?: throw Exception("User tidak ditemukan")
            val finalAvatarUrl = avatarUrl ?: existingUser.avatarUrl
            val updatedUser = existingUser.copy(
                name = name,
                avatarUrl = finalAvatarUrl
            )
            userDao.updateUser(updatedUser)
            userPreferences.saveUserInfo(userId, name, existingUser.email)
            userPreferences.saveProfilePhotoUri(finalAvatarUrl)
        }
    }
}
