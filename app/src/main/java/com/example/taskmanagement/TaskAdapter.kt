package com.example.taskmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    interface OnTaskDeleteClickListener {
        fun onTaskDeleteClick(task: Task)
    }

    private var onTaskDeleteClickListener: OnTaskDeleteClickListener? = null

    fun setOnTaskDeleteClickListener(listener: OnTaskDeleteClickListener) {
        onTaskDeleteClickListener = listener
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
        private val deleteButton: Button = itemView.findViewById(R.id.del)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = tasks[position]
                    onTaskDeleteClickListener?.onTaskDeleteClick(task)
                }
            }
        }

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            priorityTextView.text = "Priority: ${task.priority}"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            deadlineTextView.text = "Deadline: ${dateFormat.format(Date(task.deadline))}"
        }
    }
}