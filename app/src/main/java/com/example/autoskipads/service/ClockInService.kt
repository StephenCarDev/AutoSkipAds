package com.example.autoskipads.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import com.example.autoskipads.R
import com.example.autoskipads.activity.MainActivity
import com.example.autoskipads.base.appContext
import com.example.autoskipads.data.isTriggerCockIn
import com.example.autoskipads.utils.SystemTimeChangeListener
import com.example.autoskipads.utils.SystemTimeReceiver
import com.example.autoskipads.utils.atTheCurrentTime
import com.example.autoskipads.utils.errorLog
import com.example.autoskipads.utils.infoLog
import com.example.autoskipads.utils.performClickByCoordinate
import com.example.autoskipads.utils.scanAndClickByText
import com.example.autoskipads.utils.warningLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * MIX2S LineageOS
 */
class ClockInService : AccessibilityService(), SystemTimeChangeListener {

    private lateinit var mNotification: Notification

    private val receiver = SystemTimeReceiver()

    private val clockCoroutine = CoroutineScope(Dispatchers.Main)

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
        startSystemTimeListen()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.packageName == "com.android.deskclock" && !isTriggerCockIn
            && !atTheCurrentTime(9, 30, 17,30)
        ) {
            infoLog("outside trigger")
            isTriggerCockIn = true
            clockCoroutine.launch {
                delay(2000L)
                beginAutoClockIn()
            }
        }
    }

    private suspend fun beginAutoClockIn() =
        withContext(Dispatchers.Main) {
            warningLog("Clock Service triggered")
            //首先立即回到桌面
            delay(1500L)
            performGlobalAction(GLOBAL_ACTION_HOME)
            delay(1500L)
            infoLog("点通知进入系统")
            // 通过点顶部第一个通知进入系统
            performClickByCoordinate(525f, 738f)
            delay(1500L)
            infoLog("打开通知栏")
            performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
            delay(2000L)
            infoLog("关闭闹钟按钮")
            performClickByCoordinate(550f, 973f)
            delay(1500L)
            infoLog("回到桌面了")
            performGlobalAction(GLOBAL_ACTION_HOME)
            delay(1500L)
            infoLog("确保回到第一屏")
            performGlobalAction(GLOBAL_ACTION_HOME)
            delay(2000L)
            // 飞书图标
            scanAndClickByText("飞书")
            delay(4000L)
            // 工作台
            performClickByCoordinate(455f, 2024f)
            delay(4000L)
            // 假勤
            performClickByCoordinate(400f, 859f)
            // 预留6S定位
            delay(6000L)
            infoLog("开始时段判断")
            // 打卡时间判断
            if (atTheCurrentTime(8, 0, 9, 30))
            // 上午
            {
                performClickByCoordinate(550f, 811f)
                delay(1000L)
                performClickByCoordinate(550f, 800f)
                delay(1000L)
                performClickByCoordinate(550f, 740f)
                delay(1000L)
                performClickByCoordinate(550f, 830f)
            } else if (atTheCurrentTime(17, 30, 23, 30)) {
                // 下午 先点击第一次打卡
                performClickByCoordinate(550f, 1116f)
                delay(1000L)
                performClickByCoordinate(559f, 1130f)
                delay(1000L)
                performClickByCoordinate(559f, 1100f)
                delay(1500L)
                // 位置无冲突，直接触发二次刷新打卡
                performClickByCoordinate(857f, 909f)
                delay(1000L)
                performClickByCoordinate(849f, 961f)
                delay(1000L)
                performClickByCoordinate(849f, 927f)
                delay(2000L)
                performClickByCoordinate(736f, 1114f)
            } else {
                errorLog("非正常时段")
            }
            infoLog("等待打卡完成，4s后回到桌面")
            delay(4000L)
            performGlobalAction(GLOBAL_ACTION_HOME)
            // 重置标志位，锁屏
            infoLog("即将重置标志位")
            delay(1500L)
            isTriggerCockIn = false
            delay(100L)
            performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
        }

    override fun onInterrupt() {
        errorLog("onInterrupt")
    }

    private fun startSystemTimeListen() {
        infoLog("startSystemTimeListen")
        receiver.setOnSystemTimeChangeListener(this)
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(receiver, filter)
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

    override fun onTimeMatched() {
        infoLog("onTimeMatched")
//        beginAutoClockIn()
    }

    override fun onDestroy() {
        super.onDestroy()
        infoLog("==========>onDestroy<==========")
        stopForeground(true)
        receiver.releaseWakeLock(appContext)
        unregisterReceiver(receiver)
    }
}