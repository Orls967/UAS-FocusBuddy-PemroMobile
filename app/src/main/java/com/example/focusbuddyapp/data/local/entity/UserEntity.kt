package com.example.focusbuddyapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "major") val major: String = "",
    @ColumnInfo(name = "avatar_url") val avatarUrl: String = "",
    @ColumnInfo(name = "password") val password: String = "",
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
