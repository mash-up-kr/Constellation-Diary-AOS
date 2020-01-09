package com.mashup.telltostar.data.source.remote

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by hclee on 2020-01-07.
 */

interface AuthenticationNumberApi {
    @POST("/authentication-numbers")
    fun authenticationNumbers(@Body body: ReqAuthenticationNumbers): Completable

    @POST("/authentication")
    fun authentication(@Body body: ReqValidationNumber): Single<ResValidationNumber>
}