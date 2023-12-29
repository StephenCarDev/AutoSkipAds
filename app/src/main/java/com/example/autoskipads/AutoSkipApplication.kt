package com.example.autoskipads

import android.app.Application
import com.example.autoskipads.utils.LogSetting
import com.example.autoskipads.utils.MMKVHelper
import com.example.autoskipads.utils.infoLog

class AutoSkipApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        LogSetting.initLogSettings("RedfinDemo", LogSetting.LOG_VERBOSE)
        infoLog("=========>onCreate<==========")
        MMKVHelper.init("SAVETIME", false)
    }
}