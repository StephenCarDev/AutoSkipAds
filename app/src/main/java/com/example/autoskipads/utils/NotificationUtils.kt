package com.example.autoskipads.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.autoskipads.R
import com.example.autoskipads.base.appContext

object NotificationUtils {

    private const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"

    @SuppressLint("MissingPermission")
    fun sendNotification() {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("一条通知")
            .setContentText("通知内容")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(appContext)) {
            // notificationId is a unique int for each notification that you must define.
            notify(777, builder.build())
            Thread.sleep(3000L)
            cancelAll()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        val name = appContext.getString(R.string.channel_name)
        val descriptionText = appContext.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}