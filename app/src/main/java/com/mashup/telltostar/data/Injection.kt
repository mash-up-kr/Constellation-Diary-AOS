package com.mashup.telltostar.data

import android.content.Context
import com.mashup.telltostar.data.repository.DailyQuestionRepoImpl
import com.mashup.telltostar.data.repository.DiaryRepoImpl
import com.mashup.telltostar.data.repository.HoroscopeRepoImpl
import com.mashup.telltostar.data.source.DailyQuestionRepository
import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.datasource.DiaryRemoteDataSource

object Injection {

    fun provideDiaryRepo(context: Context): DiaryDataRepository {
        val remote = ApiProvider.provideDiaryApi()
        return DiaryRepoImpl(
            DiaryRemoteDataSource(remote)
        )
    }

    fun privideDiariesRepo(): DiaryDataRepository {
        val remote = ApiProvider.provideDiaryApi()
        return DiaryRemoteDataSource(remote)
    }

    fun provideHoroscopeRepo(): HoroscopeRepository {
        val remote = ApiProvider.provideHoroscopeApi()
        return HoroscopeRepoImpl(remote)
    }

    fun provideDailyQuestionRepo(): DailyQuestionRepository {
        val remote = ApiProvider.provideDailyApi()
        return DailyQuestionRepoImpl(remote)
    }
}