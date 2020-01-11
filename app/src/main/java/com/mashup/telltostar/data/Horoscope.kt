package com.mashup.telltostar.data

import android.os.Parcelable
import com.mashup.telltostar.data.source.local.entity.HoroscopeEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Horoscope(
    val contents: String
) : Parcelable

fun Horoscope.mapToEntity() = HoroscopeEntity(
    contents = contents
)