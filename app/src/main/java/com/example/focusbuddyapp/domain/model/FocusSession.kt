package com.example.focusbuddyapp.domain.model

data class FocusSession(
    val id: Int = 0,
    val linkedTaskId: Int? = null,
    val durationMinutes: Int,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val efficiencyPercent: Int? = null,
    val breakDurationMinutes: Int = 5,
    val remoteId: String? = null,
    val syncedAt: Long? = null
)
