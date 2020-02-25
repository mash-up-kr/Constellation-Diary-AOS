package com.mashup.telltostar.data.source.remote.request

data class ReqSignInDto(
    val fcmToken: String,
    val userId: String,
    val password: String
)