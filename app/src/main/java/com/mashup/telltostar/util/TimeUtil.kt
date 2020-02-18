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

    fun getDateFromUTC(utcDate: String): String {  // 2020년 1월 22일 (요일)

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

    fun get(unixTime: Long) =
        SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분 ss초").format(Date(unixTime * 1000))

}