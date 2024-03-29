package com.example.autoskipads.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.autoskipads.activity.MainActivity
import com.example.autoskipads.R
import com.example.autoskipads.base.appContext
import com.example.autoskipads.utils.debugLog
import com.example.autoskipads.utils.errorLog
import com.example.autoskipads.utils.infoLog
import com.example.autoskipads.utils.scanAndClickById
import com.example.autoskipads.utils.scanAndClickByText

class AutoSkipAdsService : AccessibilityService() {

    private lateinit var mNotification: Notification

    companion object {
        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
        const val CHANNEL_NAME = "Sample Notification"
    }

    override fun onCreate() {
        super.onCreate()
        infoLog("Device Name: ${Build.BRAND} / ${Build.MODEL}")
        startForegRround()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        debugLog("packageName: ${event.packageName}")
        scanAndClickByText("跳过")
        // csdn
        scanAndClickById("tv_skip_splashimage")
    }

    override fun onInterrupt() {
        errorLog("onInterrupt")
    }

    private fun startForegRround() {
        infoLog("startForegRround")

        createChannel(appContext)

        val notifyIntent = Intent(appContext, MainActivity::class.java)

        val title = "AutoSkipAds"
        val message = "自动跳广告前台服务"

        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("message", message)
        notifyIntent.putExtra("notification", true)

        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val pendingIntent =
            PendingIntent.getActivity(appContext, 0, notifyIntent, PendingIntent.FLAG_MUTABLE)

        mNotification = Notification.Builder(appContext, CHANNEL_ID)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setStyle(
                Notification.BigTextStyle()
                    .bigText(message)
            )
            .setContentText(message).build()

        startForeground(999, mNotification)
    }

    private fun createChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.parseColor("#e8334a")
        notificationChannel.description = "notification channel description"
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
    }

}