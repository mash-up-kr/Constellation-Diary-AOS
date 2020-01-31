package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.data.source.remote.api.HoroscopeApi
import com.mashup.telltostar.data.source.remote.response.Horoscope
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class HoroscopeRepoImpl(
    private val horoscopeApi: HoroscopeApi
) : HoroscopeRepository {

    val date = TimeUtil.getUTCDate()

    override fun get(constellation: String): Single<Horoscope> {
        return horoscopeApi.getHoroscope(constellation, date)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun get(id: Int): Single<Horoscope> {
        return horoscopeApi.getHoroscopeById(id)
            .observeOn(AndroidSchedulers.mainThread())
    }
}