package com.mashup.telltostar.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "diaries"
)
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val contents: String,

    @Embedded
    val constellation: ConstellationEntity
) {
    data class ConstellationEntity(
        val info: String
    )
}