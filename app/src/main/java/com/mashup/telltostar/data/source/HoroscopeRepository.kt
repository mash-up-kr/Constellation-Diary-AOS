package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.Horoscope
import io.reactivex.Single

interface HoroscopeRepository {

    fun get(constellation: String, date: String): Single<Horoscope>

    fun get(id: Int): Single<Horoscope>
}