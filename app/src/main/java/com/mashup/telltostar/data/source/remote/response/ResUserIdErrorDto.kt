package com.mashup.telltostar.data.source.remote.response

data class ResUserIdErrorDto(
    val error: Error
) : ResUserIdDto() {
    data class Error(
        val code: Int,
        val httpStatus: String,
        val massage: String
    )
}