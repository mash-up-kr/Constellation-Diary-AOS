package com.mashup.telltostar.data.source.remote.response

data class DiaryCount (
    val diaries : ArrayList<ResCountYearDiaryDto>,
    val timeZone : String
)