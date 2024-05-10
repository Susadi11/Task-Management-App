package com.example.taskmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ToDoActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.to_do) // Make sure this is R.layout.home
    }
}