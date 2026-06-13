package com.example.focusbuddyapp.data.remote.dto

data class TaskDto(
    val id: String,
    val title: String,
    val description: String?,
    val category: String,
    val priority: String,      // "HIGH" | "MEDIUM" | "LOW"
    val dueDate: Long?,
    val dueTime: String?,
    val isCompleted: Boolean,
    val progressPercent: Int,
    val studyNotes: String?,
    val subTasks: List<SubTaskDto>?
)

data class SubTaskDto(
    val id: String,
    val parentTaskId: String,
    val title: String,
    val isCompleted: Boolean,
    val isUrgent: Boolean
)

data class TaskCreateRequestDto(
    val title: String,
    val description: String?,
    val category: String,
    val priority: String,
    val dueDate: Long?,
    val dueTime: String?,
    val studyNotes: String?
)
