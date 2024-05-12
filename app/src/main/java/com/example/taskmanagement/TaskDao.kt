package com.example.taskmanagement

import androidx.lifecycle.LiveData
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
    fun getAll(): List<Task>

    @Query("SELECT * FROM task_table")
    fun getAllTasksLiveData(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTaskById(taskId: Long): Task

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchText || '%' OR description LIKE '%' || :searchText || '%'")
    fun searchTasks(searchText: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY title ASC")
    fun getAllSortedByTitleAsc(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY title DESC")
    fun getAllSortedByTitleDesc(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY priority DESC")
    fun getAllSortedByPriorityAsc(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY priority ASC")
    fun getAllSortedByPriorityDesc(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE date(deadline / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')")
    fun getTasksWithDeadlineToday(): List<Task>
}