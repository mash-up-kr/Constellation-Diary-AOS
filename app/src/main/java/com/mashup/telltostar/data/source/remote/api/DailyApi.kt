package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.response.DailyQuestion
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyApi {

    @GET("daily-questions")
    fun getDailyQuestions(@Query("date") date: String): Single<DailyQuestion>
}