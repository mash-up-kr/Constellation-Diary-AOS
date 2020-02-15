package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.request.ReqSignInDto
import com.mashup.telltostar.data.source.remote.request.ReqSignUpDto
import com.mashup.telltostar.data.source.remote.response.Authentication
import com.mashup.telltostar.data.source.remote.response.ResCheckUserDto
import com.mashup.telltostar.data.source.remote.response.ResUserIdDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoDto
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by hclee on 2020-01-09.
 */

interface UserApi {
    @GET("/users/check")
    fun check(@Query("user-id") userId: String): Single<ResCheckUserDto>

    @GET("/users/find-id")
    fun findId(@Query("email") email: String): Single<ResUserIdDto>

    @POST("/users/sign-in")
    fun signIn(@Header("Time-Zone") timeZone: String, @Body body: ReqSignInDto): Single<ResUserInfoDto>

    @POST("/users/sign-up")
    fun signUp(@Header("Authorization") authorization: String, @Body body: ReqSignUpDto): Single<Authentication>

    @POST("/users/sign-out")
    fun signOut(@Header("Authorization") authorization: String): Single<Response<Void>>
}