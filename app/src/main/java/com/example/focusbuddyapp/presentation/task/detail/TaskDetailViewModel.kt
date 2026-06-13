package com.example.focusbuddyapp.presentation.task.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskDetailUiState(
    val task: Task? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)

class TaskDetailViewModel(private val taskId: Int) : ViewModel() {
    private val readTaskUseCase = AppModule.readTaskUseCase
    private val deleteTaskUseCase = AppModule.deleteTaskUseCase
    private val toggleTaskCompleteUseCase = AppModule.toggleTaskCompleteUseCase

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init { loadTask() }

    private fun loadTask() = viewModelScope.launch {
        val task = readTaskUseCase(taskId)
        _uiState.update {
            if (task != null) it.copy(task = task, isLoading = false)
            else it.copy(isLoading = false, errorMessage = "Task tidak ditemukan")
        }
    }

    fun deleteTask() = viewModelScope.launch {
        deleteTaskUseCase(taskId)
        _uiState.update { it.copy(isDeleted = true) }
    }

    fun toggleComplete() = viewModelScope.launch {
        val current = _uiState.value.task ?: return@launch
        val newIsCompleted = !current.isCompleted
        val newProgress = if (newIsCompleted) 100 else 0
        toggleTaskCompleteUseCase(taskId, newIsCompleted)
        _uiState.update { it.copy(task = current.copy(isCompleted = newIsCompleted, progressPercent = newProgress)) }
    }

    fun startTimerForTask() = viewModelScope.launch {
        val current = _uiState.value.task ?: return@launch
        AppModule.userPreferences.saveActiveTask(current.id, current.title)
    }
}

class TaskDetailViewModelFactory(private val taskId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return TaskDetailViewModel(taskId) as T
    }
}
