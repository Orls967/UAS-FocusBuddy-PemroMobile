package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.domain.repository.TaskRepository

class AddTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<Long> {
        if (task.title.isBlank()) return Result.failure(Exception("Judul tugas tidak boleh kosong"))
        return runCatching { taskRepository.addTask(task) }
    }
}
