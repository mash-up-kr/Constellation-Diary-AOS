package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.UserChangeRepository
import com.mashup.telltostar.data.source.remote.api.UserChangeApi
import com.mashup.telltostar.data.source.remote.request.*
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserChangeRepoImpl(
    private val userChangeApi: UserChangeApi
) : UserChangeRepository {

    private val authorization = "Bearer ${PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")}"

    override fun setHoroscopeAlarm(flag: Boolean): Single<Any> {
        return userChangeApi.horoscopeAlarm(authorization, ReqModifyHoroscopeAlarmDto(flag))
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setHoroscopeTime(time: String): Single<Any> {
        val date = TimeUtil.getUTCTimeFromKSTTime(time)
        Timber.d("time : $time , date : $date")

        return userChangeApi.horoscopeTime(authorization, ReqModifyHoroscopeTimeDto(date))
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setQuestionAlarm(flag: Boolean): Single<Any> {
        return userChangeApi.questionAlarm(authorization, ReqModifyQuestionAlarmDto(flag))
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setQuestionTime(time: String): Single<Any> {
        val date = TimeUtil.getUTCTimeFromKSTTime(time)
        Timber.d("time : $time , date : $date")

        return userChangeApi.questionTime(authorization, ReqModifyQuestionTimeDto(date))
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setHoroscope(horoscope: String): Completable {
        return userChangeApi.constellation(authorization, ReqModifyConstellationDto(horoscope))
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}