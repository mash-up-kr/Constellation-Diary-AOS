package com.mashup.telltostar.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class Diaries(
    @SerializedName("diaries")
    val diaries: List<Diary>,
    @SerializedName("timeZone")
    val timeZone: String
)