package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.Diary
import io.reactivex.Completable
import io.reactivex.Single

interface DiaryDataSource {

    fun getDiaries(): Single<List<Diary>>

    fun getTodayDiaries(): Single<List<Diary>>

    fun getDiaryById(id: Int): Single<Diary>

    fun delete(id: Int): Completable

    fun delete(diary: Diary): Completable

    fun insert(diary: Diary): Completable

    fun update(diary: Diary): Completable

    fun clearAll(): Completable
}