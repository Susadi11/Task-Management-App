package com.example.taskmanagement

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity(), TaskAdapter.OnTaskEditClickListener {

    private lateinit var taskDao: TaskDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sortBySpinner: Spinner
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    private val sortOptions = arrayOf("Title A-Z", "Title Z-A", "Priority Low-High", "Priority High-Low")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        sortBySpinner = findViewById(R.id.sortBySpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortBySpinner.adapter = adapter

        sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = sortOptions[position]
                sortTasks(selectedOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected
            }
        }

        val taskDatabase = TaskDatabase.getDatabase(applicationContext)
        taskDao = taskDatabase.taskDao()

        recyclerView = findViewById(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(emptyList())
        recyclerView.adapter = taskAdapter

        taskAdapter.setOnTaskEditClickListener(this)

        loadTasks()

        // Register the RecyclerView for the context menu
        registerForContextMenu(recyclerView)

        val addTaskButton = findViewById<FloatingActionButton>(R.id.fab)
        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        // Initialize search EditText and addTextChangedListener
        searchEditText = findViewById(R.id.searchEditText)
        searchIcon = findViewById(R.id.searchIcon)
        searchIcon.setOnClickListener {
            toggleSearchBar()
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                filterTasks(searchText)
            }
        })
    }

    private fun toggleSearchBar() {
        if (searchEditText.visibility == View.VISIBLE) {
            searchEditText.visibility = View.GONE
            searchIcon.visibility = View.VISIBLE
        } else {
            searchEditText.visibility = View.VISIBLE
            searchIcon.visibility = View.GONE
        }
    }

    override fun onTaskEditClick(task: Task) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("taskId", task.id)
        startActivity(intent)
    }

    private fun deleteTask(task: Task) {
        lifecycleScope.launch(Dispatchers.IO) {
            taskDao.delete(task)
            val updatedTasks = taskDao.getAll()
            withContext(Dispatchers.Main) {
                taskAdapter.updateTasks(updatedTasks)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
        taskAdapter.setOnTaskDeleteClickListener(object : TaskAdapter.OnTaskDeleteClickListener {
            override fun onTaskDeleteConfirmed(task: Task) {
                deleteTask(task)
            }
        })
    }

    private fun loadTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = taskDao.getAll()
            withContext(Dispatchers.Main) {
                taskAdapter.updateTasks(tasks)
            }
        }
    }

    private fun sortTasks(selectedOption: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = when (selectedOption) {
                "Title A-Z" -> taskDao.getAllSortedByTitleAsc()
                "Title Z-A" -> taskDao.getAllSortedByTitleDesc()
                "Priority Low-High" -> taskDao.getAllSortedByPriorityAsc()
                "Priority High-Low" -> taskDao.getAllSortedByPriorityDesc()
                else -> taskDao.getAll() // Default to unsorted list
            }
            withContext(Dispatchers.Main) {
                taskAdapter.updateTasks(tasks)
            }
        }
    }

    private fun filterTasks(searchText: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val filteredTasks = if (searchText.isNotEmpty()) {
                taskDao.searchTasks("%$searchText%") // Assuming you have implemented searchTasks in TaskDao
            } else {
                taskDao.getAll()
            }
            withContext(Dispatchers.Main) {
                taskAdapter.updateTasks(filteredTasks)
            }
        }
    }
}