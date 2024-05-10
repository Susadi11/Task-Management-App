package com.example.taskmanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home) // Make sure this is R.layout.home

        val todoButton = findViewById<Button>(R.id.todo_btn)
        todoButton.setOnClickListener {
            // Start the todoActivity
            val intent = Intent(this, ToDoActivity::class.java)
            startActivity(intent)
        }
    }
}