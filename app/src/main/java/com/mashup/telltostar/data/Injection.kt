package com.mashup.telltostar.data

import android.content.Context
import com.mashup.telltostar.data.repository.*
import com.mashup.telltostar.data.source.*
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.datasource.DiaryRemoteDataSource

object Injection {

    fun provideDiaryRepo(context: Context): DiaryDataRepository {
        val remote = ApiProvider.provideDiaryApi()
        return DiaryRepoImpl(
            DiaryRemoteDataSource(remote)
        )
    }

    fun provideHoroscopeRepo(): HoroscopeRepository {
        val remote = ApiProvider.provideHoroscopeApi()
        return HoroscopeRepoImpl(remote)
    }

    fun provideDailyQuestionRepo(): DailyQuestionRepository {
        val remote = ApiProvider.provideDailyApi()
        return DailyQuestionRepoImpl(remote)
    }

    fun provideUserRepo(): UserRepository {
        val remote = ApiProvider.provideUserApi()
        return UserRepoImpl(remote)
    }

    fun provideUserChangeRepo(): UserChangeRepository {
        val remote = ApiProvider.provideUserChangeApi()
        return UserChangeRepoImpl(remote)
    }

    fun provideSignRepo(): SignRepository {
        val remote = ApiProvider.provideUserApi()
        return SignRepoImpl(remote)
    }
}