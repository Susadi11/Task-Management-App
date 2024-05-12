package com.example.taskmanagement

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class TaskReminderService : IntentService("TaskReminderService") {

    override fun onHandleIntent(intent: Intent?) {
        val taskDao = TaskDatabase.getDatabase(this).taskDao()
        val tasks = taskDao.getTasksWithDeadlineToday()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_reminder_channel"
        val channelName = "Task Reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        tasks.forEach { task ->
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Task Reminder")
                .setContentText("${task.title} is due today!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(task.id.toInt(), builder.build())
        }
    }
}