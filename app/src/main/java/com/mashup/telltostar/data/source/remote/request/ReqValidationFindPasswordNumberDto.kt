package com.mashup.telltostar.data.source.remote.request

data class ReqValidationFindPasswordNumberDto(
    val email: String,
    val number: Int,
    val userId: String
)