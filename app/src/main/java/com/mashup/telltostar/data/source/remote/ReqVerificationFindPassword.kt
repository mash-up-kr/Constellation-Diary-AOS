package com.mashup.telltostar.data.source.remote

data class ReqVerificationFindPassword(
    val email: String,
    val number: Int,
    val userId: String
)