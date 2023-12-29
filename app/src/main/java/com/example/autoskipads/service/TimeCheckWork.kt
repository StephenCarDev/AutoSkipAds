package com.example.autoskipads.service

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.autoskipads.data.SAVETME1
import com.example.autoskipads.data.SAVETME2
import com.example.autoskipads.data.SAVETME3
import com.example.autoskipads.utils.MMKVHelper
import com.example.autoskipads.utils.NotificationUtils
import com.example.autoskipads.utils.debugLog
import com.example.autoskipads.utils.infoLog
import java.text.SimpleDateFormat

class TimeCheckWork(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        infoLog("doWork")

        if (isMatchingSetTime())
            NotificationUtils.sendNotification()

        return Result.success()
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
