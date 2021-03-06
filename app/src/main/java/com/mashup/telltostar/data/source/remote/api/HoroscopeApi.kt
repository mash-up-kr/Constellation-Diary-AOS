package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.response.Horoscope
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HoroscopeApi {

    @GET("horoscopes")
    fun getHoroscope(
        @Header("Authorization") authorization: String,
        @Query("constellation") constellation: String,
        @Query("date") date: String
    ): Single<Horoscope>

    @GET("horoscopes/{id}")
    fun getHoroscopeById(
        @Header("Authorization") authorization: String,
        @Path("id") horoscopeId: Int
    ): Single<Horoscope>
}