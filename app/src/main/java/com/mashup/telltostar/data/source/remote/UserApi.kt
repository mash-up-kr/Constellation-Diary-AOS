package com.mashup.telltostar.data.source.remote

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by hclee on 2020-01-09.
 */

interface UserApi {
    @GET("/users/check")
    fun check(@Query("user-id") userId: String): Single<ResUsersCheck>

    @POST("/users/sign-in")
    fun signIn(@Body body: ReqSignIn): Single<ResSignIn>
}