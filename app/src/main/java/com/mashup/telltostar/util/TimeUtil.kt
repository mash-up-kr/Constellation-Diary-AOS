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

    fun get(unixTime: Long) =
        SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분 ss초").format(Date(unixTime * 1000))

    fun getDateInteger(unixTime: Long) =
        SimpleDateFormat("yyyyMMdd").format(Date(unixTime * 1000)).toInt()

}