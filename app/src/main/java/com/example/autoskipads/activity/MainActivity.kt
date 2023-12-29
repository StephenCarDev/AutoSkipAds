package com.example.autoskipads.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.example.autoskipads.R
import com.example.autoskipads.base.BaseActivity
import com.example.autoskipads.databinding.ActivityMainBinding
import com.example.autoskipads.utils.infoLog
import com.example.autoskipads.utils.jumpToAnotherApp

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun setBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val larkPackageName = "com.ss.android.lark"

        // 通知权限
        requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 200)

        // 跳转设置
        getBinding().btnJumptoaccessibilityservice.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        getBinding().btnJumptest.setOnClickListener {
            jumpToAnotherApp(larkPackageName)
        }

        getBinding().btnEditnotifition.setOnClickListener {
            infoLog("click")
            startActivity(Intent(this, EditActivity::class.java))
        }
    }
}