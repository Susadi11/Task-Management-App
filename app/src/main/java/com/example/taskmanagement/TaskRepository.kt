package com.example.taskmanagement

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasksLiveData()
    }

    fun getAllSortedByTitleAsc(): LiveData<List<Task>> {
        return taskDao.getAllSortedByTitleAsc()
    }

    fun getAllSortedByTitleDesc(): LiveData<List<Task>> {
        return taskDao.getAllSortedByTitleDesc()
    }

    fun getAllSortedByPriorityAsc(): LiveData<List<Task>> {
        return taskDao.getAllSortedByPriorityAsc()
    }

    fun getAllSortedByPriorityDesc(): LiveData<List<Task>> {
        return taskDao.getAllSortedByPriorityDesc()
    }

    fun searchTasks(searchText: String): LiveData<List<Task>> {
        return taskDao.searchTasks("%$searchText%")
    }
}