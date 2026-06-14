package com.example.focusbuddyapp.domain.usecase.focus

import com.example.focusbuddyapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class GetTodayFocusSummaryUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<Int> {
        return taskRepository.getAllTasks().map { tasks ->
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startOfToday = cal.timeInMillis
            val endOfToday = startOfToday + 24 * 60 * 60 * 1000L

            tasks.filter { 
                it.isCompleted && it.completedAt != null && 
                it.completedAt >= startOfToday && it.completedAt < endOfToday 
            }.sumOf { it.focusDuration }
        }
    }
}
