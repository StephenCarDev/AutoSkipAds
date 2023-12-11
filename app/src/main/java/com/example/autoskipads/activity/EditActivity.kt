package com.example.autoskipads.activity

import android.os.Bundle
import com.example.autoskipads.R
import com.example.autoskipads.base.BaseActivity
import com.example.autoskipads.data.SAVETME1
import com.example.autoskipads.data.SAVETME2
import com.example.autoskipads.data.SAVETME3
import com.example.autoskipads.databinding.ActivityEditBinding
import com.example.autoskipads.utils.MMKVHelper
import com.google.clockin.utils.debugLog
import com.google.clockin.utils.infoLog

class EditActivity : BaseActivity<ActivityEditBinding>() {

    private var alarm1Hour: String? = ""
    private var alarm2Hour: String? = ""
    private var alarm3Hour: String? = ""

    private var alarm1Min: String? = ""
    private var alarm2Min: String? = ""
    private var alarm3Min: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initGetSetTime()

        getBinding().btnSavealltime.setOnClickListener {
            infoLog("saveAllTime")

            alarm1Hour = getBinding().etAlarm1Hour.text.toString()
            alarm1Min = getBinding().etAlarm1Minute.text.toString()
            alarm2Hour = getBinding().etAlarm2Hour.text.toString()
            alarm2Min = getBinding().etAlarm2Minute.text.toString()
            alarm3Hour = getBinding().etAlarm3Hour.text.toString()
            alarm3Min = getBinding().etAlarm3Minute.text.toString()

            debugLog("alarm1Hour: $alarm1Hour, alarm1Min:$alarm1Min, alarm2Hour:$alarm2Hour, alarm2Min:$alarm2Min, alarm3Hour:$alarm3Hour, alarm3Min:$alarm3Min")

            if (alarm1Hour?.length!! > 0) {
                MMKVHelper.putString(SAVETME1, "$alarm1Hour:$alarm1Min")
                getBinding().tv1.text =
                    resources.getString(R.string.alarm1, "  -----${MMKVHelper.getString(SAVETME1)}")
            }
            if (alarm2Hour?.length!! > 0) {
                MMKVHelper.putString(SAVETME2, "$alarm2Hour:$alarm2Min")
                getBinding().tv2.text =
                    resources.getString(R.string.alarm2, "  -----${MMKVHelper.getString(SAVETME2)}")
            }
            if (alarm3Hour?.length!! > 0) {
                MMKVHelper.putString(SAVETME3, "$alarm3Hour:$alarm3Min")
                getBinding().tv3.text =
                    resources.getString(R.string.alarm3, "  -----${MMKVHelper.getString(SAVETME3)}")
            }
        }
    }

    override fun setBinding() = ActivityEditBinding.inflate(layoutInflater)

    private fun initGetSetTime() {
        infoLog("initGetSetTime")
        getBinding().tv1.text =
            resources.getString(R.string.alarm1, "  -----${MMKVHelper.getString(SAVETME1)}")
        getBinding().tv2.text =
            resources.getString(R.string.alarm2, "  -----${MMKVHelper.getString(SAVETME2)}")
        getBinding().tv3.text =
            resources.getString(R.string.alarm3, "  -----${MMKVHelper.getString(SAVETME3)}")
    }
}