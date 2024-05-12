package com.example.taskmanagement

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UndoDeleteHandler(private val context: Context) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun showUndoSnackbar(task: Task) {
        val snackbar = Snackbar.make(
            (context as HomeActivity).findViewById(android.R.id.content),
            "Task deleted",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("UNDO") {
            (context as HomeActivity).undoDelete(task)
        }
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        snackbar.show()

        launch {
            delay(5000) // 5 seconds
            snackbar.dismiss()
        }
    }
}