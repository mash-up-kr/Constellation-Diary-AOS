package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import com.mashup.telltostar.data.source.remote.response.DiaryCount
import com.mashup.telltostar.data.source.remote.response.ResCountYearDiaryDto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DiaryRepoImpl(
    private val dataSource: DiaryDataRepository
) : DiaryDataRepository {

    override fun get(id: Int): Single<Diary> {
        return dataSource.get(id)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun gets(month: Int, year: Int): Single<Diaries> {
        return dataSource.gets(month, year)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insert(
        horoscopeId: Int,
        title: String,
        content: String
    ): Single<Response<Void>> {
        return dataSource.insert(horoscopeId, title, content)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(
        id: Int,
        horoscopeId: Int,
        title: String,
        content: String
    ): Single<Any> {
        return dataSource.update(id, horoscopeId, title, content)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(id: Int): Single<Response<Void>> {
        return dataSource.delete(id)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun count(year: Int): Single<DiaryCount> {
        return dataSource.count(year)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}