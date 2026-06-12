package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return runCatching { taskRepository.deleteTask(id) }
    }
}
