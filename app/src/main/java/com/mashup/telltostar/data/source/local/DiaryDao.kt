package com.mashup.telltostar.data.source.local

import androidx.room.*
import com.mashup.telltostar.data.source.DiaryDataSource
import com.mashup.telltostar.data.source.local.entity.DiaryEntity
import io.reactivex.Single

@Dao
interface DiaryDao : DiaryDataSource {

    @Query("SELECT * FROM diaries")
    override fun getDiaries(): Single<List<DiaryEntity>>

    @Query("SELECT * FROM diaries WHERE id = :id")
    override fun getDiaryById(id: Int): Single<DiaryEntity>

    @Query("DELETE FROM diaries WHERE id = :id")
    override fun delete(id: Int)

    @Delete
    override fun delete(diary: DiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(diary: DiaryEntity)

    @Update
    override fun update(diary: DiaryEntity)

    @Query("DELETE FROM diaries")
    override fun clearAll()
}