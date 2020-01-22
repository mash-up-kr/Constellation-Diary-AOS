package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.DailyQuestion
import io.reactivex.Single

interface DailyQuestionRepository {

    fun get(date: String): Single<DailyQuestion>
}