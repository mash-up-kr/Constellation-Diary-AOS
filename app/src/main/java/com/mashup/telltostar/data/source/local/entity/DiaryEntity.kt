package com.mashup.telltostar.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mashup.telltostar.data.Constellation
import com.mashup.telltostar.data.Diary

@Entity(
    tableName = "diaries"
)
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val contents: String,
    val date: Long, // unixTime

    @Embedded
    val constellation: ConstellationEntity
) {

    data class ConstellationEntity(
        val name: String
    )

    fun ConstellationEntity.mapToPresentation() = Constellation(name = name)
}

fun DiaryEntity.mapToPresentation() = Diary(
    id = id,
    title = title,
    contents = contents,
    date = date,
    constellation = constellation.mapToPresentation()
)