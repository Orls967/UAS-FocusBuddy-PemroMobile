package com.example.focusbuddyapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.Quote
import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val userName: String = "",
    val todayFocusMinutes: Int = 0,
    val dailyGoalMinutes: Int = 150,  // 2h 30m default
    val completedTodayCount: Int = 0,
    val totalThisWeekCount: Int = 0,
    val weeklyFocusMinutes: Int = 0,
    val todayTasks: List<Task> = emptyList(),
    val quote: Quote = Quote("Focus is the art of knowing what to ignore.", "Academic Momentum"),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val profilePhotoUri: String? = null
)

class DashboardViewModel : ViewModel() {
    private val browseTasksUseCase = AppModule.browseTasksUseCase
    private val getTodayFocusSummaryUseCase = AppModule.getTodayFocusSummaryUseCase
    private val logoutUseCase = AppModule.logoutUseCase
    private val quoteRepository = AppModule.quoteRepository
    private val userPreferences = AppModule.userPreferences

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init { loadDashboard() }

    private fun loadDashboard() = viewModelScope.launch {
        // User name
        userPreferences.userName.collect { name ->
            _uiState.update { it.copy(userName = name) }
        }
    }.also {
        viewModelScope.launch {
            userPreferences.profilePhotoUri.collect { uri ->
                _uiState.update { it.copy(profilePhotoUri = uri) }
            }
        }
        viewModelScope.launch {
            browseTasksUseCase().collect { tasks ->
                _uiState.update { it.copy(todayTasks = tasks.take(4), isLoading = false) }
            }
        }
        viewModelScope.launch {
            getTodayFocusSummaryUseCase().collect { minutes ->
                _uiState.update { it.copy(todayFocusMinutes = minutes) }
            }
        }
        viewModelScope.launch {
            val quote = quoteRepository.getDailyQuote()
            _uiState.update { it.copy(quote = quote) }
        }
    }

    fun logout() = viewModelScope.launch { logoutUseCase() }
}

class DashboardViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DashboardViewModel() as T
    }
}
