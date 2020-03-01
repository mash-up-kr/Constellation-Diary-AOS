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
        val date = Date()

        // 2020-1-22T15:45:58.222Z
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.0Z'").apply {
            timeZone = utc
        }.format(date)
    }

    fun getKSTDate(): String {
        val kst = TimeZone.getTimeZone("KST")
        val date = Date()

        // 2020-1-22T15:45:58.222Z
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.0Z'").apply {
            timeZone = kst
        }.format(date)
    }

    /**
     *  2020-1-22T15:45:58.222Z + 9시간 -> 2020년 1월 22일 (요일)
     */
    fun getKSTDateFromUTCDate(utcDate: String): String {

        val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
        utcFormatter.timeZone = TimeZone.getTimeZone("UTC")

        val kstDate = utcFormatter.parse(utcDate)

        val kstFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)

        val kstFormatterDate = kstFormatter.format(kstDate)

        val totalDate = StringBuilder()

        val dates = kstFormatterDate.split("T").first().split("-")

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
            set(Calendar.MONTH, month.toInt() - 1)
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
    fun getUTCTimeFromKSTTime(time: String): String {
        val utc = TimeZone.getTimeZone("UTC")

        val date = Date()

        val times = time.split(":")

        if (times.isNullOrEmpty()) {
            return ""
        }

        val hour = times[0].toInt()
        val minute = times[1].toInt()

        val utcTime = "${getUTCHour(hour)}:${getUTCMinute(minute)}:00"

        // 2020-1-22T{time}.222Z
        return SimpleDateFormat("yyyy-MM-dd'T'").apply {
            timeZone = utc
        }.format(date).plus("${utcTime}.0Z")
    }

    private fun getUTCHour(hour: Int): String {

        var mHour = hour

        if (mHour >= 9) {
            mHour -= 9
        } else {
            mHour = 24 - (9 - mHour)
        }

        return if (mHour < 10) {
            "0$mHour"
        } else {
            mHour.toString()
        }
    }

    private fun getUTCMinute(minute: Int): String {

        return if (minute < 10) {
            "0$minute"
        } else {
            minute.toString()
        }
    }

    /**
     * 14:00 -> 23:00 : + 9시간
     */
    fun getTimePlusNine(time: String): String {
        val times = time.split(":")

        if (times.isNullOrEmpty()) {
            return ""
        }

        val hour = times[0].toInt()
        val minute = times[1].toInt()

        val strHour = if (hour > 15) {
            (hour + 9 - 24).toString()
        } else {
            (hour + 9).toString()
        }

        return "$strHour:$minute"
    }

    /**
     * 23:00 -> 14:00 : - 9시간
     */
    fun getTimeMinusNine(time: String): String {
        val times = time.split(":")

        if (times.isNullOrEmpty()) {
            return ""
        }

        val hour = times[0].toInt()
        val minute = times[1].toInt()

        val strHour = if (hour > 9) {
            (hour - 9).toString()
        } else {
            (24 - 9 + hour).toString()
        }

        return "$strHour:$minute"
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

        val minute = times[1].toInt()

        val strHour = if (hour < 10) {
            "0$hour"
        } else {
            "$hour"
        }

        val strMinute = if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        }

        totalDate.append(prefix)
        totalDate.append(strHour)
        totalDate.append(":")
        totalDate.append(strMinute)

        return totalDate.toString()
    }

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