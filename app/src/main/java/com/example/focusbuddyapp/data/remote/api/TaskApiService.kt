package com.example.focusbuddyapp.data.remote.api

import com.example.focusbuddyapp.data.remote.dto.TaskCreateRequestDto
import com.example.focusbuddyapp.data.remote.dto.TaskDto
import retrofit2.http.*

interface TaskApiService {
    @GET("tasks")
    suspend fun getAllTasks(): List<TaskDto>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: String): TaskDto

    @POST("tasks")
    suspend fun createTask(@Body request: TaskCreateRequestDto): TaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body request: TaskCreateRequestDto): TaskDto

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String)

    @PATCH("tasks/{id}/complete")
    suspend fun toggleComplete(@Path("id") id: String, @Body body: Map<String, Boolean>): TaskDto
}
