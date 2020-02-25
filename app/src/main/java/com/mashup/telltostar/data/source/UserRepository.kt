package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.Authentication
import io.reactivex.Single
import retrofit2.Response

interface UserRepository {

    fun refreshToken(): Single<Authentication.Tokens>

    fun logout(): Single<Response<Void>>
}