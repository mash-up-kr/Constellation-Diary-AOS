package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.*
import com.mashup.telltostar.data.source.remote.request.ReqFindPasswordNumberDto
import com.mashup.telltostar.data.source.remote.request.ReqValidationFindPasswordNumberDto
import com.mashup.telltostar.data.source.remote.response.ResAuthenticationTokenDto
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by hclee on 2020-01-07.
 */

interface AuthenticationNumberApi {
    @POST("/authentication-numbers/sign-up")
    fun authenticationNumbersSignUp(@Body body: ReqSignUpNumberDto): Completable

    @POST("/authentication")
    fun authentication(@Body body: ReqValidationNumber): Single<ResValidationNumber>

    @POST("/authentication-numbers/find-password")
    fun verificationNumberFindPassword(@Body body: ReqFindPasswordNumberDto): Completable

    @POST("/authentication/find-password")
    fun verificationFindPassword(@Body body: ReqValidationFindPasswordNumberDto): Single<ResAuthenticationTokenDto>
}