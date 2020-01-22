package com.mashup.telltostar.data.source.remote.api

import com.mashup.telltostar.data.source.remote.response.Horoscope
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface HoroscopeApi {

    @GET("horoscopes")
    fun getHoroscope(constellation: String, date: String): Single<Horoscope>

    @GET("horoscopes/{id}")
    fun getHoroscopeById(@Path("id") horoscopeId: Int): Single<Horoscope>
}