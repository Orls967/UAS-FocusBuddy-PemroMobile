package com.example.focusbuddyapp.domain.repository

import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: Int): Task?
    suspend fun addTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Int)
    suspend fun toggleTaskComplete(id: Int, isCompleted: Boolean)
    fun searchTasks(query: String): Flow<List<Task>>
    fun getTasksByPriority(priority: String): Flow<List<Task>>
    fun getTodayTasks(): Flow<List<Task>>
    fun getCompletedTodayCount(): Flow<Int>
    suspend fun syncWithRemote()
}
