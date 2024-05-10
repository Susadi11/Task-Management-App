package com.example.taskmanagement.data

import androidx.lifecycle.LiveData
import com.example.taskmanagement.data.Task
import com.example.taskmanagement.data.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    // Function to insert a new task into the database
    suspend fun insertTask(task: Task) {
        taskDao.insert(task)
    }

    // Function to retrieve all tasks as LiveData from the database
    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }
}
