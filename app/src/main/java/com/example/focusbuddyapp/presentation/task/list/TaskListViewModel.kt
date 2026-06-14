package com.example.focusbuddyapp.presentation.task.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TaskListUiState(
    val activeTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "ALL",   // "ALL" | "HIGH" | "MEDIUM" | "LOW"
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val userName: String = "",
    val profilePhotoUri: String? = null
)

class TaskListViewModel : ViewModel() {
    private val browseTasksUseCase = AppModule.browseTasksUseCase
    private val taskRepository = AppModule.taskRepository

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow("ALL")

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTasks() = viewModelScope.launch {
        combine(
            listOf(
                browseTasksUseCase(),
                taskRepository.getCompletedTodayTasks(),
                _searchQuery,
                _selectedFilter,
                AppModule.userPreferences.userName,
                AppModule.userPreferences.profilePhotoUri
            )
        ) { array ->
            @Suppress("UNCHECKED_CAST")
            val tasks = array[0] as List<Task>
            @Suppress("UNCHECKED_CAST")
            val completedToday = array[1] as List<Task>
            val query = array[2] as String
            val filter = array[3] as String
            val name = array[4] as String
            val photoUri = array[5] as String?

            val activeFiltered = tasks
                .filter { !it.isCompleted }
                .filter { task ->
                    val matchesQuery = query.isBlank() ||
                        task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)
                    val matchesFilter = when (filter) {
                        "HIGH"   -> task.priority.name == "HIGH"
                        "MEDIUM" -> task.priority.name == "MEDIUM"
                        "LOW"    -> task.priority.name == "LOW"
                        else     -> true
                    }
                    matchesQuery && matchesFilter
                }

            val completedFiltered = completedToday
                .filter { task ->
                    val matchesQuery = query.isBlank() ||
                        task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)
                    val matchesFilter = when (filter) {
                        "HIGH"   -> task.priority.name == "HIGH"
                        "MEDIUM" -> task.priority.name == "MEDIUM"
                        "LOW"    -> task.priority.name == "LOW"
                        else     -> true
                    }
                    matchesQuery && matchesFilter
                }

            TaskListUiState(
                activeTasks = activeFiltered,
                completedTasks = completedFiltered,
                searchQuery = query,
                selectedFilter = filter,
                isLoading = false,
                userName = name,
                profilePhotoUri = photoUri
            )
        }.collect { state ->
            _uiState.value = state
        }
    }

    fun onSearchChange(query: String) { _searchQuery.value = query }
    fun onFilterChange(filter: String) { _selectedFilter.value = filter }
}

class TaskListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TaskListViewModel() as T
    }
}
