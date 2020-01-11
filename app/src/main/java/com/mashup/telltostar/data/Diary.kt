package com.mashup.telltostar.data

import android.os.Parcelable
import com.mashup.telltostar.data.source.local.entity.DiaryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Diary(
    val id: Int = 0,
    val title: String,
    val contents: String,
    val date: Long,
    val constellation: String,
    val horoscope: Horoscope
) : Parcelable

fun Diary.mapToEntity() = DiaryEntity(
    id = id,
    title = title,
    contents = contents,
    date = date,
    constellation = constellation,
    horoscope = horoscope.mapToEntity()
)