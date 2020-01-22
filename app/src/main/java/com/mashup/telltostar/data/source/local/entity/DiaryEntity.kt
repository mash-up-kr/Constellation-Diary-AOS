package com.mashup.telltostar.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "diaries"
)
/**
 * @param id 키값
 * @param title 일기 제목
 * @param contents 일기 내용
 * @param date 날짜 ex) 2020-1-22T15:45:58.222Z
 * @param constellation 별자리명 ex) "황소자리"
 */
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    @ColumnInfo(name = "diary_contents")
    val contents: String,
    val date: String,
    val constellation: String
)