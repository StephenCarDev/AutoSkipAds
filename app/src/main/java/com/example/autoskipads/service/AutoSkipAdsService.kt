package com.example.autoskipads.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.app.NotificationCompat
import com.example.autoskipads.R
import com.example.autoskipads.base.appContext
import com.google.clockin.utils.debugLog
import com.google.clockin.utils.errorLog
import com.google.clockin.utils.infoLog

class AutoSkipAdsService : AccessibilityService() {

    override fun onCreate() {
        super.onCreate()
        infoLog("Device Name: ${Build.BRAND} / ${Build.MODEL}")
        startForegRround()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        debugLog(event.packageName.toString())
        scanAndClick("跳过")
    }

    override fun onInterrupt() {
        errorLog("onInterrupt")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        infoLog("===========>onStartCommand<=========")


        return START_STICKY
    }

    private fun startForegRround() {
        infoLog("startForegRround")
        val mBuilder =
            NotificationCompat.Builder(applicationContext!!).setAutoCancel(true) // 点击后让通知将消失
        mBuilder.setContentText("测试")
        mBuilder.setContentTitle("测试")
        mBuilder.setWhen(System.currentTimeMillis()) //通知产生的时间，会在通知信息里显示
        mBuilder.priority = Notification.PRIORITY_DEFAULT //设置该通知优先级
        mBuilder.setOngoing(false) //ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
        mBuilder.setDefaults(Notification.DEFAULT_ALL) //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
        val manager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channelId" + System.currentTimeMillis()
        val channel = NotificationChannel(
            channelId,
            resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
        mBuilder.setChannelId(channelId)
        mBuilder.setContentIntent(null)
        startForeground(222, mBuilder.build())
    }

    /**
     * 获得当前视图根节点
     * */
    private fun scanAndClick(scanText: String) = try {
        debugLog("scanAndClick scanText:$scanText")
        rootInActiveWindow?.findAccessibilityNodeInfosByText(scanText)
            .takeUnless { it.isNullOrEmpty() }?.get(0)
            ?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    } catch (e: Exception) {
        e.message?.let { errorLog(it) }
    }
}