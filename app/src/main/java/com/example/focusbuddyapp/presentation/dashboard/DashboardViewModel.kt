package com.example.focusbuddyapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.Quote
import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

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
    private val getWeeklyFocusDataUseCase = AppModule.getWeeklyFocusDataUseCase
    private val logoutUseCase = AppModule.logoutUseCase
    private val quoteRepository = AppModule.quoteRepository
    private val userPreferences = AppModule.userPreferences

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init { 
        loadDashboard() 

    }

    private fun getTodayBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        val end = start + 24 * 60 * 60 * 1000L
        return Pair(start, end)
    }

    private fun getWeekBounds(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val offset = when (dayOfWeek) {
            Calendar.SUNDAY -> -6
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> -1
            Calendar.WEDNESDAY -> -2
            Calendar.THURSDAY -> -3
            Calendar.FRIDAY -> -4
            Calendar.SATURDAY -> -5
            else -> 0
        }
        cal.add(Calendar.DAY_OF_MONTH, offset)
        val start = cal.timeInMillis
        val end = start + 7 * 24 * 60 * 60 * 1000L
        return Pair(start, end)
    }

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
                val (startOfToday, endOfToday) = getTodayBounds()
                val (startOfWeek, endOfWeek) = getWeekBounds()

                val completedToday = tasks.count {
                    it.isCompleted && it.completedAt != null && it.completedAt >= startOfToday && it.completedAt < endOfToday
                }
                val completedThisWeek = tasks.count {
                    it.isCompleted && it.completedAt != null && it.completedAt >= startOfWeek && it.completedAt < endOfWeek
                }

                // Filter tasks that are due today
                var todayTasks = tasks.filter { task ->
                    task.dueDate != null && task.dueDate >= startOfToday && task.dueDate < endOfToday
                }.take(4)

                if (todayTasks.isEmpty()) {
                    // Fallback to top active (uncompleted) tasks, ordered deterministically
                    todayTasks = tasks.filter { !it.isCompleted }
                        .sortedWith(
                            compareBy<Task> { it.dueDate == null }
                                .thenBy { it.dueDate ?: Long.MAX_VALUE }
                                .thenBy { it.priority.ordinal }
                        )
                        .take(4)
                }

                _uiState.update {
                    it.copy(
                        todayTasks = todayTasks,
                        completedTodayCount = completedToday,
                        totalThisWeekCount = completedThisWeek,
                        isLoading = false
                    )
                }
            }
        }
        viewModelScope.launch {
            getTodayFocusSummaryUseCase().collect { minutes ->
                _uiState.update { it.copy(todayFocusMinutes = minutes) }
            }
        }
        viewModelScope.launch {
            getWeeklyFocusDataUseCase().collect { weeklyData ->
                val totalMinutes = weeklyData.values.sum()
                _uiState.update { it.copy(weeklyFocusMinutes = totalMinutes) }
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
