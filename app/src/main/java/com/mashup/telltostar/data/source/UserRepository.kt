package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.request.ReqSignInDto
import com.mashup.telltostar.data.source.remote.response.Authentication
import com.mashup.telltostar.data.source.remote.response.ResUserIdDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoDto
import io.reactivex.Single
import retrofit2.Response

interface UserRepository {

    fun refreshToken(): Single<Authentication.Tokens>

    fun logout(): Single<Response<Void>>

    fun findId(email: String): Single<ResUserIdDto>

    fun signIn(timeZone: String, body: ReqSignInDto): Single<ResUserInfoDto>
}