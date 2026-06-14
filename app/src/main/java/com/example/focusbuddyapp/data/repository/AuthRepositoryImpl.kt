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
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 12)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        
        val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK)
        val offset = when (dayOfWeek) {
            java.util.Calendar.SUNDAY -> -6
            java.util.Calendar.MONDAY -> 0
            java.util.Calendar.TUESDAY -> -1
            java.util.Calendar.WEDNESDAY -> -2
            java.util.Calendar.THURSDAY -> -3
            java.util.Calendar.FRIDAY -> -4
            java.util.Calendar.SATURDAY -> -5
            else -> 0
        }
        cal.add(java.util.Calendar.DAY_OF_MONTH, offset)
        val startOfWeek = cal.timeInMillis
        val oneDay = 24 * 60 * 60 * 1000L
        val todayIndex = if (offset <= 0) -offset else 0
        val now = System.currentTimeMillis()

        fun getCompletedAt(dayIndex: Int): Long {
            return if (dayIndex == todayIndex) now else startOfWeek + dayIndex * oneDay
        }

        val taskEntities = mutableListOf<com.example.focusbuddyapp.data.local.entity.TaskEntity>()

        // 1. Beberapa active task
        taskEntities.add(
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Review Materi Mobile Programming",
                description = "Membaca kembali modul perkuliahan bab 1-8",
                category = "Kuliah", priority = "HIGH", difficulty = "HIGH", userId = userId,
                isCompleted = false
            )
        )
        taskEntities.add(
            com.example.focusbuddyapp.data.local.entity.TaskEntity(
                title = "Persiapan Presentasi UAS",
                description = "Menyiapkan slide dan materi presentasi FocusBuddy",
                category = "Presentasi", priority = "HIGH", difficulty = "MEDIUM", userId = userId,
                isCompleted = false
            )
        )

        // 2. Data Mingguan & Hari Ini sesuai distribusi (Target = 795m)
        val dailyDistributions = listOf(
            listOf(45, 15), // Mon (60m)
            listOf(60, 60), // Tue (120m)
            listOf(30, 15), // Wed (45m)
            listOf(60, 45, 75), // Thu (180m)
            listOf(45, 45), // Fri (90m)
            emptyList<Int>(), // Sat (0m)
            listOf(75, 75, 60, 90) // Sun (300m)
        )

        val dayNames = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

        dailyDistributions.forEachIndexed { dayIndex, durations ->
            durations.forEachIndexed { i, duration ->
                taskEntities.add(
                    com.example.focusbuddyapp.data.local.entity.TaskEntity(
                        title = "Pengerjaan Modul ${dayNames[dayIndex]} Bagian ${i + 1}",
                        description = "Tugas terselesaikan pada hari ${dayNames[dayIndex]} dengan durasi $duration menit.",
                        category = "Kuliah",
                        priority = "MEDIUM",
                        difficulty = "MEDIUM",
                        userId = userId,
                        isCompleted = true,
                        focusDuration = duration,
                        completedAt = getCompletedAt(dayIndex)
                    )
                )
            }
        }

        // Insert tasks and FocusSessions
        taskEntities.forEach { task ->
            val taskId = taskDao.insertTask(task).toInt()
            if (task.isCompleted && task.focusDuration > 0 && task.completedAt != null) {
                focusSessionDao.insertSession(
                    com.example.focusbuddyapp.data.local.entity.FocusSessionEntity(
                        linkedTaskId = taskId,
                        durationMinutes = task.focusDuration,
                        startTime = task.completedAt - task.focusDuration * 60 * 1000L,
                        endTime = task.completedAt
                    )
                )
            }
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