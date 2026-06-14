package com.example.focusbuddyapp.di

import android.content.Context
import com.example.focusbuddyapp.data.local.AppDatabase
import com.example.focusbuddyapp.data.preferences.UserPreferences
import com.example.focusbuddyapp.data.remote.interceptor.AuthInterceptor
import com.example.focusbuddyapp.data.remote.api.AuthApiService
import com.example.focusbuddyapp.data.remote.api.QuoteApiService
import com.example.focusbuddyapp.data.remote.api.TaskApiService
import com.example.focusbuddyapp.data.repository.AuthRepositoryImpl
import com.example.focusbuddyapp.data.repository.FocusSessionRepositoryImpl
import com.example.focusbuddyapp.data.repository.QuoteRepositoryImpl
import com.example.focusbuddyapp.data.repository.TaskRepositoryImpl
import com.example.focusbuddyapp.domain.repository.AuthRepository
import com.example.focusbuddyapp.domain.repository.FocusSessionRepository
import com.example.focusbuddyapp.domain.repository.QuoteRepository
import com.example.focusbuddyapp.domain.repository.TaskRepository
import com.example.focusbuddyapp.domain.usecase.auth.LoginUseCase
import com.example.focusbuddyapp.domain.usecase.auth.LogoutUseCase
import com.example.focusbuddyapp.domain.usecase.auth.RegisterUseCase
import com.example.focusbuddyapp.domain.usecase.task.*
import com.example.focusbuddyapp.domain.usecase.focus.GetTodayFocusSummaryUseCase
import com.example.focusbuddyapp.domain.usecase.focus.StartFocusSessionUseCase
import com.example.focusbuddyapp.domain.usecase.focus.StopFocusSessionUseCase
import com.example.focusbuddyapp.domain.usecase.analytics.GetWeeklyFocusDataUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Manual DI container (ServiceLocator pattern).
 * All dependencies are created lazily and held as singletons.
 */
object AppModule {
    private const val BASE_URL = "https://api.focusbuddy.app/v1/"
    private const val QUOTE_URL = "https://dummyjson.com/"

    lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ─── Preferences ───────────────────────────────────────────────────────
    val userPreferences: UserPreferences by lazy { UserPreferences(appContext) }

    // ─── Database ──────────────────────────────────────────────────────────
    private val database: AppDatabase by lazy { AppDatabase.getInstance(appContext) }
    val userDao by lazy { database.userDao() }
    val taskDao by lazy { database.taskDao() }
    val subTaskDao by lazy { database.subTaskDao() }
    val focusSessionDao by lazy { database.focusSessionDao() }

    // ─── Network ───────────────────────────────────────────────────────────
    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    private val authInterceptor by lazy {
        AuthInterceptor { userPreferences.getAuthTokenBlocking() }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val quoteRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(QUOTE_URL)
            .client(OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApiService: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    val taskApiService: TaskApiService by lazy { retrofit.create(TaskApiService::class.java) }
    val quoteApiService: QuoteApiService by lazy { quoteRetrofit.create(QuoteApiService::class.java) }

    // ─── Repositories ──────────────────────────────────────────────────────
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApiService, userDao, userPreferences, taskDao, focusSessionDao)
    }
    val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(taskDao, subTaskDao, taskApiService)
    }
    val focusSessionRepository: FocusSessionRepository by lazy {
        FocusSessionRepositoryImpl(focusSessionDao)
    }
    val quoteRepository: QuoteRepository by lazy {
        QuoteRepositoryImpl(quoteApiService)
    }

    // ─── Use Cases ─────────────────────────────────────────────────────────
    val loginUseCase by lazy { LoginUseCase(authRepository) }
    val registerUseCase by lazy { RegisterUseCase(authRepository) }
    val logoutUseCase by lazy { LogoutUseCase(authRepository) }

    val browseTasksUseCase by lazy { BrowseTasksUseCase(taskRepository) }
    val readTaskUseCase by lazy { ReadTaskUseCase(taskRepository) }
    val addTaskUseCase by lazy { AddTaskUseCase(taskRepository) }
    val editTaskUseCase by lazy { EditTaskUseCase(taskRepository) }
    val deleteTaskUseCase by lazy { DeleteTaskUseCase(taskRepository) }
    val searchTasksUseCase by lazy { SearchTasksUseCase(taskRepository) }
    val toggleTaskCompleteUseCase by lazy { ToggleTaskCompleteUseCase(taskRepository) }

    val startFocusSessionUseCase by lazy { StartFocusSessionUseCase(focusSessionRepository) }
    val stopFocusSessionUseCase by lazy { StopFocusSessionUseCase(focusSessionRepository) }
    val getTodayFocusSummaryUseCase by lazy { GetTodayFocusSummaryUseCase(taskRepository) }
    val getWeeklyFocusDataUseCase by lazy { GetWeeklyFocusDataUseCase(taskRepository) }
}
