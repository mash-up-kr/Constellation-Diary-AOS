package com.mashup.telltostar.data.source.remote.response

data class ResUserInfoErrorDto(
    val error: Error
) : ResUserInfoDto() {
    data class Error(
        val code: Int,
        val httpStatus: String,
        val massage: String
    )
}