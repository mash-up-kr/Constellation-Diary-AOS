package com.mashup.telltostar.data.source.local

import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.Single

class DiaryLocalDataSource(
    private val diaryDao: DiaryDao
) : DiaryDataRepository {
    override fun get(id: Int): Single<Diary> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gets(month: Int, year: Int): Single<Diaries> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(horoscopeId: Int, title: String, content: String): Single<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(id: Int, horoscopeId: Int, title: String, content: String): Single<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Int): Single<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}