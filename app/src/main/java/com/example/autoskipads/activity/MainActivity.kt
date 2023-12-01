package com.example.autoskipads.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.autoskipads.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 通知权限
        requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 200)

        // 跳转设置
        findViewById<Button>(R.id.btn_jumptoaccessibilityservice).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

    }
}