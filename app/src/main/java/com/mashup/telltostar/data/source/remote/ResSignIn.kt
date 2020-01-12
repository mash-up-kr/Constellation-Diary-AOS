package com.mashup.telltostar.data.source.remote

data class ResSignIn(
    val tokens: Tokens,
    val user: User
)

data class Tokens(
    val authenticationToken: String,
    val refreshToken: String
)

data class User(
    val constellation: String,
    val horoscopeAlarmFlag: Boolean,
    val id: Int,
    val questionAlarmFlag: Boolean,
    val questionTime: String,
    val userId: String
)