package com.example.focusbuddyapp.domain.usecase.task

import com.example.focusbuddyapp.domain.repository.TaskRepository

import kotlinx.coroutines.flow.first

class ToggleTaskCompleteUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: Int, isCompleted: Boolean) {
        taskRepository.toggleTaskComplete(id, isCompleted)
        if (isCompleted) {
            val activeId = com.example.focusbuddyapp.di.AppModule.userPreferences.activeTaskId.first()
            if (activeId == id) {
                com.example.focusbuddyapp.di.AppModule.userPreferences.clearActiveTask()
            }
        }
    }
}
