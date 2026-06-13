package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.model.Task
import com.example.focusbuddyapp.domain.repository.TaskRepository

/** BREAD: Edit — validates and updates an existing task */
class EditTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        if (task.userId <= 0) return Result.failure(Exception("Sesi tidak valid. Silakan login ulang."))
        if (task.title.isBlank()) return Result.failure(Exception("Judul tugas tidak boleh kosong"))
        return try {
            taskRepository.updateTask(task)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
