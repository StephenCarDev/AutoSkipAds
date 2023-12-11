package com.example.autoskipads.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.example.autoskipads.base.appContext
import com.example.autoskipads.data.SAVETME1
import com.example.autoskipads.data.SAVETME2
import com.example.autoskipads.data.SAVETME3
import com.google.clockin.utils.debugLog
import com.google.clockin.utils.infoLog
import java.text.SimpleDateFormat


class SystemTimeReceiver : BroadcastReceiver() {

    private lateinit var timeChangeListener: SystemTimeChangeListener

    fun setOnSystemTimeChangeListener(listener: SystemTimeChangeListener) {
        timeChangeListener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        if (action.isNullOrEmpty()) return
        //系统每1分钟发送一次广播
        if (action == Intent.ACTION_TIME_TICK && isMatchingSetTime()) {
//            NotificationUtils.sendNotification()
            lightUpScreen(appContext)
        }
    }

    private fun lightUpScreen(context: Context) {
        infoLog("lightUpScreen")
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag") val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP
                    or PowerManager.ON_AFTER_RELEASE, "TAG"
        )
        wakeLock?.acquire(10*60*1000L /*10 minutes*/)
    }

    fun releaseWakeLock(context: Context) {
        infoLog("releaseWakeLock")
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag") val wakeLock = pm.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP
                    or PowerManager.ON_AFTER_RELEASE, "TAG"
        )
        if (null != wakeLock && wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun isMatchingSetTime(): Boolean {
        val dateFormat = SimpleDateFormat("HH:mm")
        val crruenttime: String = dateFormat.format(System.currentTimeMillis())
        debugLog("当前时间：$crruenttime")
        return crruenttime == MMKVHelper.getString(SAVETME1)
                || crruenttime == MMKVHelper.getString(SAVETME2)
                || crruenttime == MMKVHelper.getString(SAVETME3)
    }
}

interface SystemTimeChangeListener {
    fun onTimeMatched()
}
