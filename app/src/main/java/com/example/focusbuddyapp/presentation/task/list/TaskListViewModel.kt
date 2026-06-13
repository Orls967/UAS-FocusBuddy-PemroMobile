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
    val allTasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "ALL",   // "ALL" | "HIGH" | "MEDIUM" | "LOW" | "DONE"
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class TaskListViewModel : ViewModel() {
    private val browseTasksUseCase = AppModule.browseTasksUseCase
    private val toggleTaskCompleteUseCase = AppModule.toggleTaskCompleteUseCase

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
            browseTasksUseCase(),
            _searchQuery,
            _selectedFilter
        ) { tasks, query, filter ->
            val filtered = tasks
                .filter { task ->
                    val matchesQuery = query.isBlank() ||
                        task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true)
                    val matchesFilter = when (filter) {
                        "HIGH"   -> task.priority.name == "HIGH"
                        "MEDIUM" -> task.priority.name == "MEDIUM"
                        "LOW"    -> task.priority.name == "LOW"
                        "DONE"   -> task.isCompleted
                        else     -> true
                    }
                    matchesQuery && matchesFilter
                }
            TaskListUiState(
                allTasks = tasks,
                filteredTasks = filtered,
                searchQuery = query,
                selectedFilter = filter,
                isLoading = false
            )
        }.collect { state ->
            _uiState.value = state
        }
    }

    fun onSearchChange(query: String) { _searchQuery.value = query }
    fun onFilterChange(filter: String) { _selectedFilter.value = filter }

    fun toggleComplete(taskId: Int, isCompleted: Boolean) = viewModelScope.launch {
        toggleTaskCompleteUseCase(taskId, isCompleted)
    }
}

class TaskListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TaskListViewModel() as T
    }
}
