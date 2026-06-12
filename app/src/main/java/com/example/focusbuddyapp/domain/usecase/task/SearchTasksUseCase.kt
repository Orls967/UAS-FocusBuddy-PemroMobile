package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class SearchTasksUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(query: String): Flow<List<Task>> = taskRepository.searchTasks(query)
}
