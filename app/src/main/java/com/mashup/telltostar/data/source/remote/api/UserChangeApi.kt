package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.request.ReqModifyConstellationDto
import com.mashup.telltostar.data.source.remote.request.ReqModifyPasswordDto
import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

/**
 * Created by hclee on 2020-01-27.
 */

interface UserChangeApi {
    @PATCH("/users/password")
    fun password(@Header("Authorization") authorization: String, @Body body: ReqModifyPasswordDto): Completable

    @PATCH("/users/constellations")
    fun constellation(@Body body: ReqModifyConstellationDto): Completable
}