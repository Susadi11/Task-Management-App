package com.example.taskmanagement

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun delete(task: Task): Int

    @Query("SELECT * FROM task_table")
    fun getAll(): List<Task> // Define getAll() function to retrieve all tasks

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTaskById(taskId: Long): Task

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchText || '%' OR description LIKE '%' || :searchText || '%'")
    fun searchTasks(searchText: String): List<Task>

}