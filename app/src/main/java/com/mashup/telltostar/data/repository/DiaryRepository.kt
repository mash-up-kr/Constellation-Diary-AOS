package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.source.DiaryDataSource
import io.reactivex.Completable
import io.reactivex.Single

class DiaryRepository(
    private val dataSource: DiaryDataSource
) : DiaryDataSource {

    override fun getDiaries(): Single<List<Diary>> {
        return dataSource.getDiaries()
    }

    override fun getTodayDiaries(): Single<List<Diary>> {
        return dataSource.getTodayDiaries()
    }

    override fun getDiaryById(id: Int): Single<Diary> {
        return dataSource.getDiaryById(id)
    }

    override fun delete(id: Int): Completable {
        return dataSource.delete(id)
    }

    override fun delete(diary: Diary): Completable {
        return dataSource.delete(diary)
    }

    override fun insert(diary: Diary): Completable {
        return dataSource.insert(diary)
    }

    override fun update(diary: Diary): Completable {
        return dataSource.update(diary)
    }

    override fun clearAll(): Completable {
        return dataSource.clearAll()
    }

}