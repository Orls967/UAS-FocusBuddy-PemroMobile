package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.repository.TaskRepository

/** BREAD: Delete — removes a task by ID */
class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: Int) = taskRepository.deleteTask(id)
}
