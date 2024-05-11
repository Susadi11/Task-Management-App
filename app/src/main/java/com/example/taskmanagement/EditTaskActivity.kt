package com.example.taskmanagement

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class EditTaskActivity : AppCompatActivity() {
    private lateinit var taskDao: TaskDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.edit_task)

        val taskDatabase = TaskDatabase.getDatabase(applicationContext)
        taskDao = taskDatabase.taskDao()

        val taskId = intent.getLongExtra("taskId", -1)
        if (taskId != -1L) {
            val task = taskDao.getTaskById(taskId)
            // Populate the views with the task data
            populateViewsWithTaskData(task)
        }
    }

    private fun populateViewsWithTaskData(task: Task) {
        // Find the views in the edit_task.xml layout and set their values with the task data
        // For example:
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        titleEditText.setText(task.title)

        // Repeat for other views like description, priority, deadline, etc.
    }
}