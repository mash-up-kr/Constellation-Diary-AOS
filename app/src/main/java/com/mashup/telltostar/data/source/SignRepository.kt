package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.Authentication
import io.reactivex.Single
import retrofit2.Response

interface SignRepository {

    fun sighUp(
        constellation: String,
        email: String,
        password: String,
        userId: String
    ): Single<Authentication>

    fun sighOut(): Single<Response<Void>>
}