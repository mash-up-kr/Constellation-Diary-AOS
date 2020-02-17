package com.mashup.telltostar.data.source.remote.response

open class ResUserInfoDto {
    lateinit var tokens: Tokens
    lateinit var user: User
}

data class Tokens(
    val authenticationToken: String,
    val refreshToken: String
)

data class User(
    val constellation: String,
    val horoscopeAlarmFlag: Boolean,
    val horoscopeTime: String,
    val id: Int,
    val questionAlarmFlag: Boolean,
    val questionTime: String,
    val userId: String
)