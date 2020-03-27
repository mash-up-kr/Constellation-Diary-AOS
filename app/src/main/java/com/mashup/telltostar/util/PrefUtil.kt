package com.mashup.telltostar.util

import android.content.Context
import android.content.SharedPreferences

object PrefUtil {

    const val PREF_NAME = "Pref"

    const val CONSTELLATION = "constellation"

    const val HOROSCOPE_ALARM_FLAG = "horoscope_alarm_flag"
    const val HOROSCOPE_TIME = "horoscope_time"

    const val QUESTION_ALARM_FLAG = "question_alarm_flag"
    const val QUESTION_TIME = "question_time"

    const val AUTHENTICATION_TOKEN = "authenticationToken"
    const val REFRESH_TOKEN = "refreshToken"

    const val EXPLAIN_MAIN_HOROSCOPE = "explain_main_horoscope"

    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        pref = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun get(key: String, defValue: String): String {
        return pref.getString(key, defValue)
    }

    fun get(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun get(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun put(key: String, value: String) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun put(key: String, value: Int) {
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun put(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun remove(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clear() {
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }
}