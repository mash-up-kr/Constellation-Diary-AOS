package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.request.*
import io.reactivex.Completable
import io.reactivex.Single
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
    fun constellation(@Header("Authorization") authorization: String, @Body body: ReqModifyConstellationDto): Completable

    @PATCH("/users/horoscope-alarm")
    fun horoscopeAlarm(@Header("Authorization") authorization: String, @Body body: ReqModifyHoroscopeAlarmDto): Single<Any>

    @PATCH("/users/horoscope-time")
    fun horoscopeTime(@Header("Authorization") authorization: String, @Body body: ReqModifyHoroscopeTimeDto): Single<Any>

    @PATCH("/users/question-alarm")
    fun questionAlarm(@Header("Authorization") authorization: String, @Body body: ReqModifyQuestionAlarmDto): Single<Any>

    @PATCH("/users/question-time")
    fun questionTime(@Header("Authorization") authorization: String, @Body body: ReqModifyQuestionTimeDto): Single<Any>
}