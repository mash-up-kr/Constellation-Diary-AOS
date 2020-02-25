package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.request.DiaryDto
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface DiaryApi {

    @GET("diaries")
    fun getDiaries(
        @Header("Authorization") authorization: String,
        @Query("month") month: Int, @Query("year") year: Int
    ): Single<Diaries>

    @GET("diaries/{id}")
    fun getDiary(
        @Header("Authorization") authorization: String,
        @Path("id") diaryId: Int
    ): Single<Diary>

    @POST("diaries")
    fun postDiaries(
        @Header("Authorization") authorization: String,
        @Body diaryDto: DiaryDto
    ): Single<Response<Void>>

    @PATCH("diaries/{id}")
    fun putDiary(
        @Header("Authorization") authorization: String,
        @Path("id") diaryId: Int,
        @Body diaryDto: DiaryDto
    ): Single<Any>

    @DELETE("diaries/{id}")
    fun deleteDiary(
        @Header("Authorization") authorization: String,
        @Path("id") diaryId: Int
    ): Single<Response<Void>>
}
