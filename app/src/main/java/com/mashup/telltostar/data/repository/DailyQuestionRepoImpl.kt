package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.DailyQuestionRepository
import com.mashup.telltostar.data.source.remote.api.DailyApi
import com.mashup.telltostar.data.source.remote.response.DailyQuestion
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class DailyQuestionRepoImpl(
    private val dailyApi: DailyApi
) : DailyQuestionRepository {

    override fun get(): Single<DailyQuestion> {

        val date = TimeUtil.getDate()

        return dailyApi.getDailyQuestions(date)
            .observeOn(AndroidSchedulers.mainThread())
    }
}