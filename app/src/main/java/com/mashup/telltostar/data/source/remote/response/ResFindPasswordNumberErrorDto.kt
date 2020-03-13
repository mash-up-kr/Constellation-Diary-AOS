package com.mashup.telltostar.data.source.remote.response

data class ResFindPasswordNumberErrorDto(
    val error: Error
) {
    data class Error(
        val code: Int,
        val httpStatus: String,
        val massage: String
    )
}