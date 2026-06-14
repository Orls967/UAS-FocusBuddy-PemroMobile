package com.example.focusbuddyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "focus_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["linked_task_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("linked_task_id"), Index("end_time")]
)
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "linked_task_id") val linkedTaskId: Int? = null,
    @ColumnInfo(name = "duration_minutes") val durationMinutes: Int,
    @ColumnInfo(name = "start_time") val startTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time") val endTime: Long? = null,
    @ColumnInfo(name = "break_duration_minutes") val breakDurationMinutes: Int = 5,
    @ColumnInfo(name = "remote_id") val remoteId: String? = null,
    @ColumnInfo(name = "synced_at") val syncedAt: Long? = null
)
