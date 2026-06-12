package com.example.focusbuddyapp.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val major: String = "",
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
