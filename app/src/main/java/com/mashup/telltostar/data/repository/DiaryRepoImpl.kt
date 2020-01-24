package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

class DiaryRepoImpl(
    private val dataRepository: DiaryDataRepository
) : DiaryDataRepository {

    override fun get(id: Int): Single<Diary> {
        return dataRepository.get(id)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun gets(month: Int, year: Int): Single<Diaries> {
        return dataRepository.gets(month, year)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insert(
        horoscopeId: Int,
        title: String,
        content: String,
        date: String
    ): Single<Any> {
        return dataRepository.insert(horoscopeId, title, content, date)
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(
        id: Int,
        horoscopeId: Int,
        title: String,
        content: String,
        date: String
    ): Single<Any> {
        return dataRepository.update(id, horoscopeId, title, content, date)
    }

    override fun delete(id: Int): Single<Any> {
        return dataRepository.delete(id)
            .observeOn(AndroidSchedulers.mainThread())
    }
}