package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.*
import com.mashup.telltostar.data.source.remote.request.ReqSignInDto
import com.mashup.telltostar.data.source.remote.response.ResUserIdDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoDto
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by hclee on 2020-01-09.
 */

interface UserApi {
    @GET("/users/check")
    fun check(@Query("user-id") userId: String): Single<ResUsersCheck>

    @GET("/users/find-id")
    fun findId(@Query("email") email: String): Single<ResUserIdDto>

    @POST("/users/sign-in")
    fun signIn(@Header("Time-Zone") timeZone: String, @Body body: ReqSignInDto): Single<ResUserInfoDto>

    @PATCH("/users/password")
    fun password(@Header("Authorization") authorization: String, @Body body: ReqResetPassword): Completable
}