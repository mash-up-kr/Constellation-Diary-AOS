package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.data.source.remote.api.HoroscopeApi
import com.mashup.telltostar.data.source.remote.response.Horoscope
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HoroscopeRepoImpl(
    private val horoscopeApi: HoroscopeApi
) : HoroscopeRepository {

    val authorization = "Bearer ${PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")}"
    val date = TimeUtil.getUTCDate()

    override fun get(constellation: String): Single<Horoscope> {
        return horoscopeApi.getHoroscope(authorization, constellation, date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun get(id: Int): Single<Horoscope> {
        return horoscopeApi.getHoroscopeById(authorization, id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}