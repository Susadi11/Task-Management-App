package com.example.taskmanagement

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao: TaskDao
    private val taskRepository: TaskRepository

    init {
        val database = TaskDatabase.getDatabase(application)
        taskDao = database.taskDao()
        taskRepository = TaskRepository(taskDao)
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return taskRepository.getAllTasks()
    }

    fun getAllSortedByTitleAsc(): LiveData<List<Task>> {
        return taskRepository.getAllSortedByTitleAsc()
    }

    fun getAllSortedByTitleDesc(): LiveData<List<Task>> {
        return taskRepository.getAllSortedByTitleDesc()
    }

    fun getAllSortedByPriorityAsc(): LiveData<List<Task>> {
        return taskRepository.getAllSortedByPriorityAsc()
    }

    fun getAllSortedByPriorityDesc(): LiveData<List<Task>> {
        return taskRepository.getAllSortedByPriorityDesc()
    }

    fun searchTasks(searchText: String): LiveData<List<Task>> {
        return taskRepository.searchTasks(searchText)
    }

    fun insertTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.delete(task)
        }
    }

    suspend fun getTaskById(taskId: Long): Task = withContext(Dispatchers.IO) {
        taskDao.getTaskById(taskId)
    }
}