package com.example.taskmanagement.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.data.Task
import com.example.taskmanagement.data.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Function to insert a new task
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    // Function to get all tasks as LiveData
    fun getAllTasks(): LiveData<List<Task>> {
        return repository.getAllTasks()
    }
}
