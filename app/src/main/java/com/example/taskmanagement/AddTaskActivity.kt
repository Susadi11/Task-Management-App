package com.example.taskmanagement

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskDao: TaskDao
    private lateinit var selectedDateTextView: TextView
    private lateinit var selectedPriority: Priority

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task)

        selectedDateTextView = findViewById(R.id.selectedDateTextView)

        val taskDatabase = TaskDatabase.getDatabase(applicationContext)
        taskDao = taskDatabase.taskDao()

        val priorityRadioGroup = findViewById<RadioGroup>(R.id.priorityRadioGroup)
        priorityRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedPriority = when (radioButton.id) {
                R.id.lowPriorityRadioButton -> Priority.LOW
                R.id.mediumPriorityRadioButton -> Priority.MEDIUM
                R.id.highPriorityRadioButton -> Priority.HIGH
                else -> Priority.LOW // Default to low priority if none selected
            }
            Log.d("AddTaskActivity", "Selected Priority: $selectedPriority")
        }
    }

    fun saveTask(view: View) {
        val title = findViewById<EditText>(R.id.titleEditText).text.toString().trim()
        val description = findViewById<EditText>(R.id.descriptionEditText).text.toString().trim()

        // Check if a date is selected
        if (selectedDateTextView.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select a deadline", Toast.LENGTH_SHORT).show()
            return
        }

        val deadline = selectedDateTextView.text.toString()

        // Parse the selected date into milliseconds
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(deadline)
        val deadlineMillis = date?.time ?: System.currentTimeMillis()

        // Ensure selectedPriority is assigned as a Priority enum
        val task = Task(title = title, description = description, priority = selectedPriority, deadline = deadlineMillis)
        insertTask(task)
    }



    private fun insertTask(task: Task) {
        lifecycleScope.launch(Dispatchers.IO) {
            taskDao.insertTask(task)
            Log.d("AddTaskActivity", "Task inserted: $task")

            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Task saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun showDatePicker(view: View) {
        val dateRangePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { timestamp ->
            val selectedDate = formatDate(timestamp)
            selectedDateTextView.text = selectedDate
        }

        dateRangePicker.show(supportFragmentManager, "datePicker")
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}