package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.local.entity.DiaryEntity
import io.reactivex.Single

interface DiaryDataSource {

    fun getDiaries(): Single<List<DiaryEntity>>

    fun getDiaryById(id: Int): Single<DiaryEntity>

    fun delete(id: Int)

    fun delete(diary: DiaryEntity)

    fun insert(diary: DiaryEntity)

    fun update(diary: DiaryEntity)

    fun clearAll()
}