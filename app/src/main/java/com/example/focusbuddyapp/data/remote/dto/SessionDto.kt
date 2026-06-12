package com.example.focusbuddyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FocusSessionDto(
    @SerializedName("id") val id: String,
    @SerializedName("linked_task_id") val linkedTaskId: String? = null,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("start_time") val startTime: Long,
    @SerializedName("end_time") val endTime: Long? = null,
    @SerializedName("efficiency_percent") val efficiencyPercent: Int? = null,
    @SerializedName("break_duration_minutes") val breakDurationMinutes: Int = 5
)

data class FocusSessionCreateDto(
    @SerializedName("linked_task_id") val linkedTaskId: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("start_time") val startTime: Long,
    @SerializedName("end_time") val endTime: Long?,
    @SerializedName("efficiency_percent") val efficiencyPercent: Int?,
    @SerializedName("break_duration_minutes") val breakDurationMinutes: Int = 5
)

data class QuoteDto(
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: String
)
