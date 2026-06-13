package com.example.focusbuddyapp.presentation.task.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.focusbuddyapp.di.AppModule
import com.example.focusbuddyapp.domain.model.Priority
import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "Academic Focus",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val studyNotes: String = "",
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

class AddEditTaskViewModel(private val taskId: Int?) : ViewModel() {
    private val readTaskUseCase = AppModule.readTaskUseCase
    private val addTaskUseCase = AppModule.addTaskUseCase
    private val editTaskUseCase = AppModule.editTaskUseCase

    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    init {
        if (taskId != null) loadTask(taskId)
    }

    private fun loadTask(id: Int) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val task = readTaskUseCase(id)
        if (task != null) {
            _uiState.update {
                it.copy(
                    title = task.title,
                    description = task.description,
                    category = task.category,
                    priority = task.priority,
                    dueDate = task.dueDate,
                    studyNotes = task.studyNotes,
                    isEditMode = true,
                    isLoading = false
                )
            }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Task tidak ditemukan") }
        }
    }

    fun onTitleChange(v: String) = _uiState.update { it.copy(title = v, errorMessage = null) }
    fun onDescriptionChange(v: String) = _uiState.update { it.copy(description = v) }
    fun onCategoryChange(v: String) = _uiState.update { it.copy(category = v) }
    fun onPriorityChange(v: Priority) = _uiState.update { it.copy(priority = v) }
    fun onDueDateChange(v: Long?) = _uiState.update { it.copy(dueDate = v) }
    fun onStudyNotesChange(v: String) = _uiState.update { it.copy(studyNotes = v) }

    fun save() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val state = _uiState.value
        val task = Task(
            id = taskId ?: 0,
            title = state.title,
            description = state.description,
            category = state.category,
            priority = state.priority,
            dueDate = state.dueDate,
            studyNotes = state.studyNotes
        )
        val result = if (state.isEditMode) editTaskUseCase(task) else addTaskUseCase(task).map { Unit }
        result.fold(
            onSuccess = { _uiState.update { it.copy(isLoading = false, isSaved = true) } },
            onFailure = { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        )
    }
}

class AddEditTaskViewModelFactory(private val taskId: Int?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AddEditTaskViewModel(taskId) as T
    }
}
