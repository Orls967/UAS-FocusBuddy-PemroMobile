package com.example.focusbuddyapp.domain.repository

import com.example.focusbuddyapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    // BREAD operations
    fun getAllTasks(): Flow<List<Task>>                          // Browse
    suspend fun getTaskById(id: Int): Task?                      // Read
    suspend fun addTask(task: Task): Long                        // Add
    suspend fun updateTask(task: Task)                           // Edit
    suspend fun deleteTask(id: Int)                              // Delete

    // Extras
    suspend fun toggleTaskComplete(id: Int, isCompleted: Boolean)
    fun searchTasks(query: String): Flow<List<Task>>
    fun getTasksByPriority(priority: String): Flow<List<Task>>
    fun getTodayTasks(): Flow<List<Task>>
    fun getCompletedTodayCount(): Flow<Int>

    // Sync
    suspend fun syncWithRemote()
}
