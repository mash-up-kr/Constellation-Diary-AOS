package com.mashup.telltostar.data.source.remote.request

data class ReqSignUpDto(
    val constellation: String,
    val email: String,
    val password: String,
    val userId: String
)