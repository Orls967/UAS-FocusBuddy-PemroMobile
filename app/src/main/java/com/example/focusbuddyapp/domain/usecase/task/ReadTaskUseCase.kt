package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.domain.repository.TaskRepository

/** BREAD: Read — returns a single task by ID */
class ReadTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: Int): Task? = taskRepository.getTaskById(id)
}
