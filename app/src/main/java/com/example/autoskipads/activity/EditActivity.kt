package com.example.autoskipads.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.autoskipads.R
import com.example.autoskipads.base.BaseActivity
import com.example.autoskipads.data.SAVETME1
import com.example.autoskipads.data.SAVETME2
import com.example.autoskipads.data.SAVETME3
import com.example.autoskipads.databinding.ActivityEditBinding
import com.example.autoskipads.utils.MMKVHelper
import com.google.clockin.utils.infoLog

class EditActivity : BaseActivity<ActivityEditBinding>() {

    var alarm1Hour: String? = ""
    var alarm2Hour: String? = ""
    var alarm3Hour: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initGetSetTime()

        getBinding().etAlarm1Hour.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    infoLog("test")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    alarm1Hour = getBinding().etAlarm1Hour.text.toString()
                }

            }
        )

        getBinding().etAlarm2Hour.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    infoLog("test")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    alarm2Hour = getBinding().etAlarm2Hour.text.toString()
                }

            }
        )

        getBinding().etAlarm3Hour.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    infoLog("test")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    alarm3Hour = getBinding().etAlarm3Hour.text.toString()
                }

            }
        )

        getBinding().etAlarm1Minute.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                infoLog("test")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                MMKVHelper.putString(SAVETME1, "$alarm1Hour:${getBinding().etAlarm1Minute.text}")
            }

        })

        getBinding().etAlarm2Minute.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                infoLog("test")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                MMKVHelper.putString(SAVETME2, "$alarm2Hour:${getBinding().etAlarm2Minute.text}")
            }

        })

        getBinding().etAlarm3Minute.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                infoLog("test")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                MMKVHelper.putString(SAVETME3, "$alarm3Hour:${getBinding().etAlarm3Minute.text}")
            }

        })
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