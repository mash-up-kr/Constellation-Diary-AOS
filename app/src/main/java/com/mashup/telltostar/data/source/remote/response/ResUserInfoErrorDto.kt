package com.mashup.telltostar.data.source.remote.response

data class ResUserInfoErrorDto(
    val error: Error
) : BaseResUserInfoDto() {
    data class Error(
        val code: Int,
        val httpStatus: String,
        val massage: String
    )
}