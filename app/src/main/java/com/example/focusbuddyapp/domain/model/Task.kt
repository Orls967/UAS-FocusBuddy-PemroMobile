package com.example.focusbuddyapp.domain.model

enum class Priority { HIGH, MEDIUM, LOW }

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val category: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val dueTime: String? = null,
    val isCompleted: Boolean = false,
    val progressPercent: Int = 0,
    val studyNotes: String = "",
    val remoteId: String? = null,
    val syncedAt: Long? = null,
    val userId: Int = 1,
    val subTasks: List<SubTask> = emptyList()
)
