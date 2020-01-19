package com.mashup.telltostar.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashup.telltostar.data.Diary

@Entity(
    tableName = "diaries"
)
/**
 * @param id 키값
 * @param title 일기 제목
 * @param contents 일기 내용
 * @param date 유닉스타임
 * @param constellation 별자리명 ex) "황소자리"
 * @param horoscope 별자리운세
 */
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    @ColumnInfo(name = "diary_contents") val contents: String,
    val date: String, // unixTime
    val constellation: String, // "황소자리"

    @Embedded
    val horoscope: HoroscopeEntity
)

fun DiaryEntity.mapToPresentation() = Diary(
    id = id,
    title = title,
    contents = contents,
    date = date,
    constellation = constellation,
    horoscope = horoscope.mapToPresentation()
)