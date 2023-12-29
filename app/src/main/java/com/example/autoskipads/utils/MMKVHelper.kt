package com.example.autoskipads.utils

import com.example.autoskipads.base.appContext
import com.tencent.mmkv.MMKV

/**
 * 最适合同步写入小数据
 * 支持多进程，高频写入性能好
 * 但有可能丢数据
 */
object MMKVHelper {

    private lateinit var mmkv: MMKV

    fun init(databaseId: String, isMultiProcess: Boolean) {
        val rootDir = MMKV.initialize(appContext)
        infoLog("MMKV rootDir: $rootDir")
        mmkv =
            if (isMultiProcess) MMKV.mmkvWithID(databaseId, MMKV.MULTI_PROCESS_MODE)
            else MMKV.mmkvWithID(databaseId)
    }

    fun putString(key: String, value: String?) {
        infoLog("putString key: $key, value: $value")
        mmkv.encode(key, value)
    }

    fun getString(key: String): String? {
        val value = mmkv.decodeString(key)
        infoLog("getString key: $key, value: $value")
        return value
    }

    fun getString(key: String, defaultValue: String): String {
        val value = mmkv.decodeString(key)
        infoLog("getString key: $key, value: $value, defaultValue: $defaultValue")
        return value ?: defaultValue
    }

    fun putLong(key: String?, value: Long) {
        infoLog("putLong key: $key, value: $value")
        mmkv.encode(key, value)
    }

    fun putFloat(key: String?, value: Float) {
        infoLog("putFloat key: $key, value: $value")
        mmkv.encode(key, value)
    }

    fun putInt(key: String?, value: Int) {
        infoLog("putInt key: $key, value: $value")
        mmkv.encode(key, value)
    }

    fun putBoolean(key: String?, value: Boolean) {
        infoLog("putBoolean key: $key, value: $value")
        mmkv.encode(key, value)
    }

    fun getLong(key: String?): Long {
        val value = mmkv.decodeLong(key, -1)
        infoLog("getLong key: $key, value: $value")
        return value
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        val value = mmkv.decodeInt(key, defaultValue)
        infoLog("getInt key: $key, value: $value")
        return value
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        val value = mmkv.decodeFloat(key, defaultValue)
        infoLog("getFloat key: $key, value: $value")
        return value
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        val value = mmkv.decodeBool(key, defaultValue)
        infoLog("getBoolean key: $key, value: $value")
        return value
    }
}
