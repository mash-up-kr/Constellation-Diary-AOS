package com.mashup.telltostar.data.source

import io.reactivex.Completable
import io.reactivex.Single

interface UserChangeRepository {

    fun setHoroscopeAlarm(flag: Boolean): Single<Any>

    fun setHoroscopeTime(time: String): Single<Any>

    fun setQuestionAlarm(flag: Boolean): Single<Any>

    fun setQuestionTime(time: String): Single<Any>

    fun setHoroscope(horoscope:String): Completable
}