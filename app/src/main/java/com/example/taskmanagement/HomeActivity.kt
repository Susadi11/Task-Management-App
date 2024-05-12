package com.example.taskmanagement

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity(), TaskAdapter.OnTaskEditClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sortBySpinner: Spinner
    private lateinit var searchEditText: EditText
    private lateinit var searchIcon: ImageView
    private val sortOptions = arrayOf("Title A-Z", "Title Z-A", "Priority Low-High", "Priority High-Low")
    private lateinit var undoDeleteHandler: UndoDeleteHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val imageDelButton = findViewById<ImageButton>(R.id.imageSettingButton)

        // Set OnClickListener for the imageDelButton
        imageDelButton.setOnClickListener {
            // Navigate to SettingActivity when imageDelButton is clicked
            val intent = Intent(this@HomeActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        startService(Intent(this, TaskReminderService::class.java))

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        undoDeleteHandler = UndoDeleteHandler(this)

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
        viewModel.deleteTask(task)
        undoDeleteHandler.showUndoSnackbar(task)
    }

    fun undoDelete(task: Task) {
        viewModel.insertTask(task)
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
        viewModel.getAllTasks().observe(this, Observer { tasks ->
            taskAdapter.updateTasks(tasks)
        })
    }

    private fun sortTasks(selectedOption: String) {
        when (selectedOption) {
            "Title A-Z" -> viewModel.getAllSortedByTitleAsc().observe(this, Observer { tasks ->
                taskAdapter.updateTasks(tasks)
            })
            "Title Z-A" -> viewModel.getAllSortedByTitleDesc().observe(this, Observer { tasks ->
                taskAdapter.updateTasks(tasks)
            })
            "Priority Low-High" -> viewModel.getAllSortedByPriorityAsc().observe(this, Observer { tasks ->
                taskAdapter.updateTasks(tasks)
            })
            "Priority High-Low" -> viewModel.getAllSortedByPriorityDesc().observe(this, Observer { tasks ->
                taskAdapter.updateTasks(tasks)
            })
        }
    }

    private fun filterTasks(searchText: String) {
        viewModel.searchTasks(searchText).observe(this, Observer { tasks ->
            taskAdapter.updateTasks(tasks)
        })
    }
}