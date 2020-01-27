package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.ReqResetPassword
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

/**
 * Created by hclee on 2020-01-27.
 */

interface UserChangeApi {
    @PATCH("/users/password")
    fun password(@Header("Authorization") authorization: String, @Body body: ReqResetPassword): Completable
}