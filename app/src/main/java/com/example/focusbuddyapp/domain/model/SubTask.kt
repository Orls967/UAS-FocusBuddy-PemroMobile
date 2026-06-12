package com.example.focusbuddyapp.domain.model

data class SubTask(
    val id: Int = 0,
    val parentTaskId: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isUrgent: Boolean = false
)
