package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.Authentication
import com.mashup.telltostar.data.source.remote.response.ResCheckUserDto
import io.reactivex.Single
import retrofit2.Response

interface SignRepository {

    fun sighUp(
        constellation: String,
        email: String,
        fcmToken: String,
        password: String,
        userId: String,
        token: String
    ): Single<Authentication>

    fun sighOut(): Single<Response<Void>>

    fun check(userId: String): Single<ResCheckUserDto>
}