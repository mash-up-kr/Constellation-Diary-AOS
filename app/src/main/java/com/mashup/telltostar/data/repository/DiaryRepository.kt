package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.DiaryDataSource
import com.mashup.telltostar.data.source.local.entity.DiaryEntity
import io.reactivex.Single

class DiaryRepository(
    private val diaryDataSource: DiaryDataSource
) : DiaryDataSource {

    override fun getDiaries(): Single<List<DiaryEntity>> {
        return diaryDataSource.getDiaries()
    }

    override fun getDiaryById(id: Int): Single<DiaryEntity> {
        return diaryDataSource.getDiaryById(id)
    }

    override fun delete(id: Int) {
        diaryDataSource.delete(id)
    }

    override fun delete(diary: DiaryEntity) {
        diaryDataSource.delete(diary)
    }

    override fun insert(diary: DiaryEntity) {
        diaryDataSource.insert(diary)
    }

    override fun update(diary: DiaryEntity) {
        diaryDataSource.update(diary)
    }

    override fun clearAll() {
        diaryDataSource.clearAll()
    }
}