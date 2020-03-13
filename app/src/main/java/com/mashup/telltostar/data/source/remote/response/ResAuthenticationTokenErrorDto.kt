package com.mashup.telltostar.data.source.remote.response

data class ResAuthenticationTokenErrorDto(
    val error: Error
) : ResAuthenticationTokenDto() {
    data class Error(
        val code: Int,
        val httpStatus: String,
        val massage: String
    )
}