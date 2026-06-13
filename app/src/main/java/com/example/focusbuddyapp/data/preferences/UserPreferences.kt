package com.example.focusbuddyapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "focusbuddy_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val AUTH_TOKEN     = stringPreferencesKey("auth_token")
        val USER_ID        = intPreferencesKey("user_id")
        val USER_NAME      = stringPreferencesKey("user_name")
        val USER_EMAIL     = stringPreferencesKey("user_email")
        val POMODORO_MIN   = intPreferencesKey("pomodoro_minutes")
        val BREAK_MIN      = intPreferencesKey("break_minutes")
        val NOTIF_ENABLED  = booleanPreferencesKey("notifications_enabled")
        val PROFILE_PHOTO_URI = stringPreferencesKey("profile_photo_uri")
        val ACTIVE_TASK_ID = intPreferencesKey("active_task_id")
        val ACTIVE_TASK_TITLE = stringPreferencesKey("active_task_title")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    // Auth token
    val authToken: Flow<String?> = context.dataStore.data.map { it[AUTH_TOKEN] }
    fun getAuthTokenBlocking(): String? = runCatching {
        runBlocking {
            context.dataStore.data.map { it[AUTH_TOKEN] }.firstOrNull()
        }
    }.getOrNull()

    val userId: Flow<Int> = context.dataStore.data.map { it[USER_ID] ?: 0 }
    val userName: Flow<String> = context.dataStore.data.map { it[USER_NAME] ?: "" }
    val pomodoroMinutes: Flow<Int> = context.dataStore.data.map { it[POMODORO_MIN] ?: 25 }
    val breakMinutes: Flow<Int> = context.dataStore.data.map { it[BREAK_MIN] ?: 5 }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[NOTIF_ENABLED] ?: true }
    val profilePhotoUri: Flow<String?> = context.dataStore.data.map { it[PROFILE_PHOTO_URI] }
    val activeTaskId: Flow<Int> = context.dataStore.data.map { it[ACTIVE_TASK_ID] ?: 0 }
    val activeTaskTitle: Flow<String> = context.dataStore.data.map { it[ACTIVE_TASK_TITLE] ?: "" }
    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE] ?: false }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[AUTH_TOKEN] = token }
    }

    suspend fun saveUserInfo(id: Int, name: String, email: String) {
        context.dataStore.edit {
            it[USER_ID]    = id
            it[USER_NAME]  = name
            it[USER_EMAIL] = email
        }
    }

    suspend fun saveLoginSession(token: String, id: Int, name: String, email: String) {
        context.dataStore.edit {
            it[AUTH_TOKEN] = token
            it[USER_ID]    = id
            it[USER_NAME]  = name
            it[USER_EMAIL] = email
        }
    }


    suspend fun saveFocusSettings(pomodoroMin: Int, breakMin: Int) {
        context.dataStore.edit {
            it[POMODORO_MIN] = pomodoroMin
            it[BREAK_MIN]    = breakMin
        }
    }

    suspend fun saveNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIF_ENABLED] = enabled }
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun saveProfilePhotoUri(uri: String?) {
        context.dataStore.edit {
            if (uri != null) {
                it[PROFILE_PHOTO_URI] = uri
            } else {
                it.remove(PROFILE_PHOTO_URI)
            }
        }
    }

    suspend fun saveActiveTask(id: Int, title: String) {
        context.dataStore.edit {
            it[ACTIVE_TASK_ID] = id
            it[ACTIVE_TASK_TITLE] = title
        }
    }

    suspend fun clearActiveTask() {
        context.dataStore.edit {
            it.remove(ACTIVE_TASK_ID)
            it.remove(ACTIVE_TASK_TITLE)
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
