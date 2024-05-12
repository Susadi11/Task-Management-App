package com.example.taskmanagement

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnTaskDeleteClickListener {
        fun onTaskDeleteConfirmed(task: Task)
    }

    interface OnTaskEditClickListener {
        fun onTaskEditClick(task: Task)
    }

    private var onTaskDeleteClickListener: OnTaskDeleteClickListener? = null

    fun setOnTaskDeleteClickListener(listener: OnTaskDeleteClickListener) {
        onTaskDeleteClickListener = listener
    }

    private var onTaskEditClickListener: OnTaskEditClickListener? = null

    fun setOnTaskEditClickListener(listener: OnTaskEditClickListener) {
        onTaskEditClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Task? {
        if (position in 0 until tasks.size) {
            return tasks[position]
        }
        return null
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val priorityTextView: TextView = itemView.findViewById(R.id.priorityTextView)
        private val deadlineTextView: TextView = itemView.findViewById(R.id.deadlineTextView)
        private val imageDelButton: ImageButton = itemView.findViewById(R.id.imageDelButton)
        private val imageEditButton: ImageButton = itemView.findViewById(R.id.imageEditButton)

        init {
            imageDelButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = tasks[position]
                    showDeleteConfirmationDialog(task)
                }
            }

            imageEditButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = tasks[position]
                    onTaskEditClickListener?.onTaskEditClick(task)
                }
            }
        }

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            priorityTextView.text = "Priority: ${task.priority.name}"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            deadlineTextView.text = "Deadline: ${dateFormat.format(Date(task.deadline))}"
        }

        private fun showDeleteConfirmationDialog(task: Task) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("YES") { _, _ ->
                    onTaskDeleteClickListener?.onTaskDeleteConfirmed(task)
                }
                .setNegativeButton("NO", null)
                .show()
        }
    }
}