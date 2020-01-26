package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.request.DiaryDto
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.Single
import retrofit2.http.*

interface DiaryApi {

    @GET("diaries")
    fun getDiaries(@Query("month") month: Int, @Query("year") year: Int): Single<Diaries>

    @GET("diaries/{id}")
    fun getDiary(@Path("id") diaryId: Int): Single<Diary>

    @POST("diaries")
    fun postDiaries(@Body diaryDto: DiaryDto): Single<Any>

    @PUT("diaries/{id}")
    fun putDiary(@Path("id") diaryId: Int, @Body diaryDto: DiaryDto): Single<Any>

    @DELETE("diaries/{id}")
    fun deleteDiary(@Path("id") diaryId: Int): Single<Any>
}
