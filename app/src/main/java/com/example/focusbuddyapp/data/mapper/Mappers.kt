package com.example.focusbuddyapp.data.mapper

import com.example.focusbuddyapp.data.local.entity.TaskEntity
import com.example.focusbuddyapp.data.local.entity.SubTaskEntity
import com.example.focusbuddyapp.data.local.entity.UserEntity
import com.example.focusbuddyapp.data.local.entity.FocusSessionEntity
import com.example.focusbuddyapp.data.remote.dto.*
import com.example.focusbuddyapp.domain.model.*

// ─── Entity → Domain ─────────────────────────────────────────────────────────

fun UserEntity.toDomain() = User(
    id = id, name = name, email = email, major = major,
    avatarUrl = avatarUrl, createdAt = createdAt
)

fun TaskEntity.toDomain(subTasks: List<SubTask> = emptyList()) = Task(
    id = id, title = title, description = description, category = category,
    priority = Priority.valueOf(priority),
    difficulty = Difficulty.valueOf(difficulty),
    dueDate = dueDate, dueTime = dueTime,
    isCompleted = isCompleted, progressPercent = progressPercent,
    studyNotes = studyNotes, remoteId = remoteId, syncedAt = syncedAt,
    userId = userId, subTasks = subTasks,
    completedAt = completedAt, focusDuration = focusDuration
)

fun SubTaskEntity.toDomain() = SubTask(
    id = id, parentTaskId = parentTaskId, title = title,
    isCompleted = isCompleted, isUrgent = isUrgent
)

fun FocusSessionEntity.toDomain() = FocusSession(
    id = id, linkedTaskId = linkedTaskId, durationMinutes = durationMinutes,
    startTime = startTime, endTime = endTime,
    breakDurationMinutes = breakDurationMinutes, remoteId = remoteId, syncedAt = syncedAt
)

// ─── Domain → Entity ─────────────────────────────────────────────────────────

fun User.toEntity() = UserEntity(
    id = id, name = name, email = email, major = major,
    avatarUrl = avatarUrl, createdAt = createdAt
)

fun Task.toEntity() = TaskEntity(
    id = id, title = title, description = description, category = category,
    priority = priority.name,
    difficulty = difficulty.name,
    dueDate = dueDate, dueTime = dueTime,
    isCompleted = isCompleted, progressPercent = progressPercent,
    studyNotes = studyNotes, remoteId = remoteId, syncedAt = syncedAt, userId = userId,
    completedAt = completedAt, focusDuration = focusDuration
)

fun SubTask.toEntity() = SubTaskEntity(
    id = id, parentTaskId = parentTaskId, title = title,
    isCompleted = isCompleted, isUrgent = isUrgent
)

fun FocusSession.toEntity() = FocusSessionEntity(
    id = id, linkedTaskId = linkedTaskId, durationMinutes = durationMinutes,
    startTime = startTime, endTime = endTime,
    breakDurationMinutes = breakDurationMinutes, remoteId = remoteId, syncedAt = syncedAt
)

// ─── DTO → Domain ─────────────────────────────────────────────────────────────

fun UserDto.toDomain() = User(
    id = 0, name = name, email = email,
    major = major ?: "", avatarUrl = avatarUrl ?: ""
)

fun TaskDto.toDomain() = Task(
    id = 0, title = title, description = description ?: "",
    category = category,
    priority = Priority.valueOf(priority),
    difficulty = Difficulty.valueOf(difficulty),
    dueDate = dueDate, dueTime = dueTime,
    isCompleted = isCompleted, progressPercent = progressPercent,
    studyNotes = studyNotes ?: "",
    remoteId = id,
    subTasks = subTasks?.map { it.toDomain() } ?: emptyList(),
    completedAt = null, focusDuration = 25
)

fun SubTaskDto.toDomain() = SubTask(
    id = 0, parentTaskId = 0, title = title,
    isCompleted = isCompleted, isUrgent = isUrgent
)

fun QuoteDto.toDomain() = Quote(content = quote, author = author ?: "Unknown")
