package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.DailyQuestionRepository
import com.mashup.telltostar.data.source.remote.api.DailyApi
import com.mashup.telltostar.data.source.remote.response.DailyQuestion
import io.reactivex.Single

class DailyQuestionRepoImpl(
    private val dailyApi: DailyApi
) : DailyQuestionRepository {

    override fun get(date: String): Single<DailyQuestion> {
        return dailyApi.getDailyQuestions(date)
    }
}