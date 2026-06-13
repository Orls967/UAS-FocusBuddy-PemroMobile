package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.repository.TaskRepository

class ToggleTaskCompleteUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: Int, isCompleted: Boolean) =
        taskRepository.toggleTaskComplete(id, isCompleted)
}
