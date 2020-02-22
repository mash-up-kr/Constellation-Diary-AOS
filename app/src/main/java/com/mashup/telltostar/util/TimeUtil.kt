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

    fun getUTCDate(): String {
        val utc = TimeZone.getTimeZone("UTC")
        //val time = TimeZone.getDefault()
        val date = Date()

        // 2020-1-22T15:45:58.222Z
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.0Z'").apply {
            timeZone = utc
        }.format(date)
    }

    /**
     *  2020-1-22T15:45:58.222Z -> 2020년 1월 22일 (요일)
     */
    fun getDateFromUTC(utcDate: String): String {

        val totalDate = StringBuilder()

        val dates = utcDate.split("T").first().split("-")

        var year = ""
        var month = ""
        var day = ""

        for ((index, date) in dates.withIndex()) {

            when (index) {
                0 -> year = date
                1 -> month = date
                2 -> day = date
            }

            totalDate.append("${date}${dateValue(index)} ")
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year.toInt())
            set(Calendar.MONTH, month.toInt())
            set(Calendar.DATE, day.toInt())
        }

        val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        totalDate.append("($dayOfWeek)")

        return totalDate.toString()
    }

    /**
     *  14:00:00 -> 2020-1-22T14:00:00.222Z
     *
     *  여기서 오는 time 은 한국 시간이므로 -9를 해서 utc로 변환해 줍니다.
     */
    fun getUTCFromTime(time: String): String {
        val utc = TimeZone.getTimeZone("UTC")
        //val time = TimeZone.getDefault()
        val date = Date()

        val times = time.split(":")
        var hour = times[0].toInt()
        var strHour = ""

        if (hour >= 9) {
            hour -= 9
        } else {
            hour = 24 - (9 - hour)
        }

        if (hour < 10) {
            strHour = "0$hour"
        } else {
            strHour = hour.toString()
        }

        val minute = times[1].toInt()
        var strMinute = ""

        if (minute < 10) {
            strMinute = "0$strMinute"
        } else {
            strMinute = minute.toString()
        }

        val utcTime = "$strHour:$strMinute:00"

        // 2020-1-22T{time}.222Z
        return SimpleDateFormat("yyyy-MM-dd'T'").apply {
            timeZone = utc
        }.format(date).plus("${utcTime}.0Z")
    }

    /**
     *  14:00:00 -> 오후2:00
     */
    fun getAlarmFromTime(time: String): String {
        val totalDate = StringBuilder()

        val times = time.split(":")

        if (times.isNullOrEmpty()) {
            return ""
        }

        var prefix = "오전 "

        var hour = times[0].toInt()

        if (hour > 12) {
            hour -= 12
            prefix = "오후 "
        }

        totalDate.append(prefix)
        totalDate.append(hour)
        totalDate.append(":")
        totalDate.append(times[1])

        return totalDate.toString()
    }

    /**
     *
     */

    private fun dateValue(pos: Int) = when (pos) {
        0 -> "년"
        1 -> "월"
        2 -> "일"
        else -> ""
    }

    // 일요일 1 ~ 토요일 7
    private fun getDayOfWeek(pos: Int) = when (pos) {
        1 -> "일"
        2 -> "월"
        3 -> "화"
        4 -> "수"
        5 -> "목"
        6 -> "금"
        7 -> "토"
        else -> ""
    }
}