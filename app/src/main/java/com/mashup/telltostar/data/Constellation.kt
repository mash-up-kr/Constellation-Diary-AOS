package com.mashup.telltostar.data

import android.os.Parcelable
import com.mashup.telltostar.data.source.local.entity.DiaryEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Constellation(val name: String) : Parcelable

fun Constellation.mapToEntity() = DiaryEntity.ConstellationEntity(name = name)