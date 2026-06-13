package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.domain.repository.TaskRepository

/** BREAD: Add — validates and inserts a new task */
class AddTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<Long> {
        if (task.title.isBlank()) return Result.failure(Exception("Judul tugas tidak boleh kosong"))
        return try {
            val id = taskRepository.addTask(task)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
