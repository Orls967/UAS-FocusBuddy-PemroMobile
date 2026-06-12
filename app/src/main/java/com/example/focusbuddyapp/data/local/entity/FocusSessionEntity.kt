package com.example.focusbuddyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "linked_task_id") val linkedTaskId: Int?,
    @ColumnInfo(name = "duration_minutes") val durationMinutes: Int,
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "end_time") val endTime: Long?,
    @ColumnInfo(name = "efficiency_percent") val efficiencyPercent: Int?,
    @ColumnInfo(name = "break_duration_minutes") val breakDurationMinutes: Int = 5,
    @ColumnInfo(name = "remote_id") val remoteId: String? = null,
    @ColumnInfo(name = "synced_at") val syncedAt: Long? = null
)
