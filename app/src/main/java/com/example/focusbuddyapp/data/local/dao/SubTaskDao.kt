package com.example.focusbuddyapp.data.local.dao

import androidx.room.*
import com.example.focusbuddyapp.data.local.entity.SubTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTaskEntity): Long

    @Update
    suspend fun updateSubTask(subTask: SubTaskEntity)

    @Query("SELECT * FROM subtasks WHERE parent_task_id = :parentTaskId")
    fun getSubTasksForTask(parentTaskId: Int): Flow<List<SubTaskEntity>>

    @Query("DELETE FROM subtasks WHERE id = :id")
    suspend fun deleteSubTask(id: Int)
}
