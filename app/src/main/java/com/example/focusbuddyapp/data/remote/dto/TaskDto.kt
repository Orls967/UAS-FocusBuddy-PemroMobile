package com.example.focusbuddyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SubTaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("is_urgent") val isUrgent: Boolean
)

data class TaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("category") val category: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("due_date") val dueDate: Long? = null,
    @SerializedName("due_time") val dueTime: String? = null,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("progress_percent") val progressPercent: Int,
    @SerializedName("study_notes") val studyNotes: String? = null,
    @SerializedName("sub_tasks") val subTasks: List<SubTaskDto>? = null
)

data class TaskCreateRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("priority") val priority: String,
    @SerializedName("due_date") val dueDate: Long?,
    @SerializedName("due_time") val dueTime: String?,
    @SerializedName("study_notes") val studyNotes: String
)
