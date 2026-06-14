package com.example.focusbuddyapp.domain.usecase.analytics

import com.example.focusbuddyapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class GetWeeklyFocusDataUseCase(private val taskRepository: TaskRepository) {
    /** Returns a map of day abbreviation to minutes focused, e.g. {"MON" -> 45, "TUE" -> 120} */
    operator fun invoke(): Flow<Map<String, Int>> {
        return taskRepository.getAllTasks().map { tasks ->
            val result = mutableMapOf(
                "MON" to 0, "TUE" to 0, "WED" to 0, 
                "THU" to 0, "FRI" to 0, "SAT" to 0, "SUN" to 0
            )
            
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            val offset = when (dayOfWeek) {
                Calendar.SUNDAY -> -6
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> -1
                Calendar.WEDNESDAY -> -2
                Calendar.THURSDAY -> -3
                Calendar.FRIDAY -> -4
                Calendar.SATURDAY -> -5
                else -> 0
            }
            cal.add(Calendar.DAY_OF_MONTH, offset)
            val startOfWeek = cal.timeInMillis
            val endOfWeek = startOfWeek + 7 * 24 * 60 * 60 * 1000L

            val format = SimpleDateFormat("EEE", Locale.ENGLISH)
            
            tasks.filter { 
                it.isCompleted && it.completedAt != null && 
                it.completedAt >= startOfWeek && it.completedAt < endOfWeek 
            }.forEach { task ->
                val dayCal = Calendar.getInstance().apply { timeInMillis = task.completedAt!! }
                val dayKey = format.format(dayCal.time).uppercase(Locale.ENGLISH)
                if (result.containsKey(dayKey)) {
                    result[dayKey] = (result[dayKey] ?: 0) + task.focusDuration
                }
            }
            
            result
        }
    }
}
