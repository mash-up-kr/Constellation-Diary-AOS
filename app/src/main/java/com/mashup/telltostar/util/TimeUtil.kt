package com.mashup.telltostar.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    //https://currentmillis.com/tutorials/system-currentTimeMillis.html
    fun getCurrentUnixTime(): Long {
        val millis = System.currentTimeMillis()

        // prints a Unix timestamp in milliseconds
        //return millis

        // prints the same Unix timestamp in seconds
        return millis / 1000
    }

    private val date = Date()

    fun getDate(): String {
        //val time = TimeZone.getDefault()
        //TODO 2020-1-22T15:45:58.222Z
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.0Z'").format(date)
    }

    fun get(unixTime: Long) =
        SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분 ss초").format(Date(unixTime * 1000))

}