package com.livin.androidmvvmtodoapp.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.data.repositories.todo.TodoItemRepository
import com.livin.androidmvvmtodoapp.utils.Converters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var todoItemRepository: TodoItemRepository

    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val todoId = intent.extras?.getInt("todo_id")
        todoId?.let {
            runBlocking {
                val todoItem = todoItemRepository.getTodoItemById(todoId)
                createNotificationChannel(context)
                todoItem?.let { item -> notifyNotification(context, item) }
            }
        }
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Todo",
                NotificationManager.IMPORTANCE_HIGH
            )

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context, todoItem: TodoItem) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(todoItem.title)
                .setSubText(
                    Converters.fromTimestampToDate(todoItem.date) + ", " + Converters.fromTimestampToTime(
                        todoItem.time
                    )
                )
                .setContentText(todoItem.description)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notify(NOTIFICATION_ID, build.build())
        }
    }
}
