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
import com.example.autoskipads.R
import com.example.autoskipads.activity.MainActivity
import com.example.autoskipads.base.appContext
import com.example.autoskipads.utils.atTheCurrentTime
import com.example.autoskipads.utils.performClickByCoordinate
import com.google.clockin.utils.debugLog
import com.google.clockin.utils.errorLog
import com.google.clockin.utils.infoLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClockInService : AccessibilityService() {

    private var isTrigger = false

    private lateinit var mNotification: Notification

    companion object {
        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
        const val CHANNEL_NAME = "Sample Notification"
    }

    //    performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)//切换到分屏
    //    performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS)//打开快速设置，暂时不知道这个有什么用
    //    performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)//打开通知栏
    //    performGlobalAction(GLOBAL_ACTION_BACK)//模拟back键
    //    performGlobalAction(GLOBAL_ACTION_HOME)//模拟home键
    //    performGlobalAction(GLOBAL_ACTION_RECENTS)//模拟最近任务键
    //    performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)//打开电源键长按对话框
    //    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)//锁屏
    //    performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT)//截图
    override fun onCreate() {
        super.onCreate()
        infoLog("===>onCreate<=== Device Name: ${Build.BRAND} / ${Build.MODEL}")
        startForegRroundService()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == "com.android.deskclock" && !isTrigger
            && !atTheCurrentTime(9, 0, 17, 0)
        ) {
            isTrigger = true
            CoroutineScope(Dispatchers.IO).launch {
                //首先立即回到桌面
                performGlobalAction(GLOBAL_ACTION_HOME)
                delay(2000L)
                // 确保回到第一屏
                performGlobalAction(GLOBAL_ACTION_HOME)
                delay(500L)
                performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
                delay(1000L)
                // 关闭闹钟
                performClickByCoordinate(this@ClockInService, 874f, 997f)
                delay(500L)
                performGlobalAction(GLOBAL_ACTION_HOME)
                delay(1000L)
                // 飞书图标
                performClickByCoordinate(this@ClockInService, 645f, 979f)
                delay(4000L)
                // 工作台图标
                performClickByCoordinate(this@ClockInService, 466f, 2062f)
                delay(1500L)
                // 假勤
                performClickByCoordinate(this@ClockInService, 400f, 862f)
                debugLog("定位预留6s")
                delay(6000L)
                debugLog("开始时段判断")
                // 打卡时间判断
                if (atTheCurrentTime(7, 0, 8, 30))
                // 上午
                    performClickByCoordinate(this@ClockInService, 554f, 830f)
                else if (atTheCurrentTime(17, 30, 18, 0))
                // 下午
                    performClickByCoordinate(this@ClockInService, 874f, 997f)
                else if (atTheCurrentTime(18, 1, 23, 30)) {
                    // 二次打卡
                    performClickByCoordinate(this@ClockInService, 861f, 964f)
                    delay(1000L)
                    performClickByCoordinate(this@ClockInService, 725f, 1187f)
                }
                debugLog("等待打卡完成")
                delay(4000L)
                performGlobalAction(GLOBAL_ACTION_HOME)
                // 重置标志位，锁屏
                debugLog("1s后锁屏")
                delay(1000L)
                isTrigger = false
                performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
            }
        }
    }

    override fun onInterrupt() {
        errorLog("onInterrupt")
    }

    override fun onDestroy() {
        super.onDestroy()
        infoLog("==========>onDestroy<==========")
        stopForeground(true)
    }

    /**
     * 获得当前视图根节点
     * */
    private fun scanAndClick(scanText: String) = try {
        debugLog("scanAndClick scanText:$scanText")
        rootInActiveWindow?.findAccessibilityNodeInfosByText(scanText)
            .takeUnless { it.isNullOrEmpty() }?.get(0)
            ?.run {
                debugLog(this.className.toString())
                performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
    } catch (e: Exception) {
        e.message?.let { errorLog(it) }
    }

    private fun startForegRroundService() {
        infoLog("startForegRroundService")

        createChannel(appContext)

        val notifyIntent = Intent(appContext, MainActivity::class.java)

        val title = "辅助服务"
        val message = "前台服务"

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

        startForeground(888, mNotification)
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