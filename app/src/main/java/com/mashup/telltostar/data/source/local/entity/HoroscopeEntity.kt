package com.mashup.telltostar.data.source.local.entity

import com.mashup.telltostar.data.Horoscope

/**
 * @param contents 별자리운세 내용
 */
data class HoroscopeEntity(
    val contents: String
)

fun HoroscopeEntity.mapToPresentation() = Horoscope(
    contents = contents
)