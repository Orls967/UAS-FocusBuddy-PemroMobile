package com.example.focusbuddyapp.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val category: String = "Academic Focus",
    val priority: Priority = Priority.MEDIUM,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val dueDate: Long? = null,
    val dueTime: String? = null,        // "HH:mm"
    val isCompleted: Boolean = false,
    val progressPercent: Int = 0,       // 0–100
    val studyNotes: String = "",
    val remoteId: String? = null,       // server UUID for sync
    val syncedAt: Long? = null,
    val userId: Int = 0,
    val subTasks: List<SubTask> = emptyList(),
    val completedAt: Long? = null,
    val focusDuration: Int = 25
)

enum class Priority { HIGH, MEDIUM, LOW }
enum class Difficulty { HIGH, MEDIUM, LOW }

