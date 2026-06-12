package com.example.focusbuddyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "parent_task_id") val parentTaskId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "is_urgent") val isUrgent: Boolean = false
)
