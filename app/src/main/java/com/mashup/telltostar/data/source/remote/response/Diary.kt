package com.mashup.telltostar.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class Diary(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("horoscopeId")
    val horoscopeId: Int = -1,
    @SerializedName("date")
    val date: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("content")
    val content: String = ""

)