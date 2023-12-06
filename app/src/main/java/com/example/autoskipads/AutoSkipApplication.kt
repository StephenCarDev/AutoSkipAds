package com.example.autoskipads

import android.app.Application
import com.google.clockin.utils.infoLog

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

    }
}