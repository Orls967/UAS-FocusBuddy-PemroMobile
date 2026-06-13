package com.example.focusbuddyapp.data.remote.dto

data class FocusSessionDto(
    val id: String,
    val linkedTaskId: String?,
    val durationMinutes: Int,
    val startTime: Long,
    val endTime: Long?,
    val efficiencyPercent: Int?,
    val breakDurationMinutes: Int
)

data class QuoteDto(
    val content: String,
    val author: String
)
