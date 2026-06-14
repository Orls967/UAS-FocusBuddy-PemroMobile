package com.example.focusbuddyapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.focusbuddyapp.data.local.dao.FocusSessionDao
import com.example.focusbuddyapp.data.local.dao.SubTaskDao
import com.example.focusbuddyapp.data.local.dao.TaskDao
import com.example.focusbuddyapp.data.local.dao.UserDao
import com.example.focusbuddyapp.data.local.entity.FocusSessionEntity
import com.example.focusbuddyapp.data.local.entity.SubTaskEntity
import com.example.focusbuddyapp.data.local.entity.TaskEntity
import com.example.focusbuddyapp.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        SubTaskEntity::class,
        FocusSessionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun subTaskDao(): SubTaskDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focusbuddy.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
