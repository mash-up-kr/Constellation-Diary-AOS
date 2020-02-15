package com.mashup.telltostar.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Authentication(
    @SerializedName("tokens")
    val tokens: Tokens,
    @SerializedName("user")
    val user: User
) {
    data class Tokens(
        @SerializedName("authenticationToken")
        val authenticationToken: String,
        @SerializedName("refreshToken")
        val refreshToken: String
    )

    data class User(
        @SerializedName("constellation")
        val constellation: String,
        @SerializedName("horoscopeAlarmFlag")
        val horoscopeAlarmFlag: Boolean,
        @SerializedName("horoscopeTime")
        val horoscopeTime: HoroscopeTime,
        @SerializedName("id")
        val id: Int,
        @SerializedName("questionAlarmFlag")
        val questionAlarmFlag: Boolean,
        @SerializedName("questionTime")
        val questionTime: QuestionTime,
        @SerializedName("timeZone")
        val timeZone: String,
        @SerializedName("userId")
        val userId: String
    ) {
        data class HoroscopeTime(
            @SerializedName("hour")
            val hour: Int,
            @SerializedName("minute")
            val minute: Int,
            @SerializedName("nano")
            val nano: Int,
            @SerializedName("second")
            val second: Int
        )

        data class QuestionTime(
            @SerializedName("hour")
            val hour: Int,
            @SerializedName("minute")
            val minute: Int,
            @SerializedName("nano")
            val nano: Int,
            @SerializedName("second")
            val second: Int
        )
    }
}