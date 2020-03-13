package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.DailyQuestionRepository
import com.mashup.telltostar.data.source.remote.api.DailyApi
import com.mashup.telltostar.data.source.remote.response.DailyQuestion
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DailyQuestionRepoImpl(
    private val dailyApi: DailyApi
) : DailyQuestionRepository {

    override fun get(): Single<DailyQuestion> {

        val authorization = "Bearer ${PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")}"
        val date = TimeUtil.getUTCDate()

        return dailyApi.getDailyQuestions(authorization = authorization, date = date)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}