package com.mashup.telltostar.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashup.telltostar.data.Constellation

@Entity(
    tableName = "diaries"
)
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val contents: String,
    //별자리 운세
    val constellation: Constellation
)