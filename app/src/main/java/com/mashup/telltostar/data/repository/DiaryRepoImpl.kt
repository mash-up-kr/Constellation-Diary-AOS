package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response

class DiaryRepoImpl(
    private val dataSource: DiaryDataRepository
) : DiaryDataRepository {

    override fun get(id: Int): Single<Diary> {
        return dataSource.get(id)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun gets(month: Int, year: Int): Single<Diaries> {
        return dataSource.gets(month, year)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insert(
        horoscopeId: Int,
        title: String,
        content: String
    ): Single<Response<Void>> {
        return dataSource.insert(horoscopeId, title, content)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(
        id: Int,
        horoscopeId: Int,
        title: String,
        content: String
    ): Single<Any> {
        return dataSource.update(id, horoscopeId, title, content)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(id: Int): Single<Response<Void>> {
        return dataSource.delete(id)
            .observeOn(AndroidSchedulers.mainThread())
    }
}