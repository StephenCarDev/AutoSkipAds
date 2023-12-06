package com.example.autoskipads.utils

import android.content.ComponentName
import android.content.Intent
import com.example.autoskipads.base.appContext

fun jumpToAnotherApp(packageName: String, activityName: String? = null) {
    appContext.run {
        startActivity(
            if (activityName != null)
                Intent().setComponent(ComponentName(packageName, activityName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            else
                packageManager.getLaunchIntentForPackage(packageName)
                    ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}