package com.example.taskmanagement

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

        taskAdapter.setOnTaskDeleteClickListener(object : TaskAdapter.OnTaskDeleteClickListener {
            override fun onTaskDeleteClick(task: Task) {
                deleteTask(task)
            }
        })

        taskAdapter.setOnTaskEditClickListener(this)

        loadTasks()

        // Register the RecyclerView for the context menu
        registerForContextMenu(recyclerView)

        val addTaskButton = findViewById<FloatingActionButton>(R.id.fab)
        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onTaskEditClick(task: Task) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("taskId", task.id)
        startActivity(intent)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.task_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as? AdapterView.AdapterContextMenuInfo)?.position ?: return super.onContextItemSelected(item)
        val task = taskAdapter.getItem(position) ?: return super.onContextItemSelected(item)

        return when (item.itemId) {
            R.id.action_edit -> {
                // Handle edit action
                true
            }
            R.id.action_delete -> {
                // Handle delete action
                deleteTask(task)
                true
            }
            else -> super.onContextItemSelected(item)
        }
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
    }

    private fun loadTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val tasks = taskDao.getAll()
            runOnUiThread {
                taskAdapter.updateTasks(tasks)
            }
        }
    }

    private fun sortTasks(selectedOption: String) {
        // Implement sorting logic based on the selected option
        // For example, you can update the RecyclerView adapter with sorted data
    }
}