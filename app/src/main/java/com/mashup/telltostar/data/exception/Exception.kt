package com.mashup.telltostar.data.exception

sealed class Exception(message: String) : Throwable(message) {

    class AuthenticationException(message: String, code: Int) : Exception(message)

    class OverRefreshException(message: String, code: Int) : Exception(message)

    class ExistEmailException(message: String, code: Int) : Exception(message)
}