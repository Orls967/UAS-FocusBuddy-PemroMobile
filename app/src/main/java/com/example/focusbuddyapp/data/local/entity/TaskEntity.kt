package com.example.focusbuddyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "priority") val priority: String,
    @ColumnInfo(name = "due_date") val dueDate: Long?,
    @ColumnInfo(name = "due_time") val dueTime: String?,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "progress_percent") val progressPercent: Int = 0,
    @ColumnInfo(name = "study_notes") val studyNotes: String = "",
    @ColumnInfo(name = "remote_id") val remoteId: String? = null,
    @ColumnInfo(name = "synced_at") val syncedAt: Long? = null,
    @ColumnInfo(name = "user_id") val userId: Int = 1
)
