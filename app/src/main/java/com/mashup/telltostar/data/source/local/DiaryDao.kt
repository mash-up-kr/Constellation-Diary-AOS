package com.mashup.telltostar.data.source.local

import androidx.room.*
import com.mashup.telltostar.data.source.local.entity.DiaryEntity
import io.reactivex.Single

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diaries ORDER BY date ASC")
    fun getDiaries(): Single<List<DiaryEntity>>

    @Query("SELECT * FROM diaries WHERE id = :id")
    fun getDiaryById(id: Int): Single<DiaryEntity>

    @Query("DELETE FROM diaries WHERE id = :id")
    fun delete(id: Int)

    @Delete
    fun delete(diary: DiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(diary: DiaryEntity)

    @Update
    fun update(diary: DiaryEntity)

    @Query("DELETE FROM diaries")
    fun clearAll()
}