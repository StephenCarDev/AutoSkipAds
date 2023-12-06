package com.example.autoskipads

import android.app.Application
import android.util.Log
import com.example.autoskipads.utils.MMKVHelper
import com.google.clockin.utils.infoLog
import com.tencent.mmkv.MMKV

class AutoSkipApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        infoLog("=========>onCreate<==========")
        MMKVHelper.init("SAVETIME", false)

    }
}