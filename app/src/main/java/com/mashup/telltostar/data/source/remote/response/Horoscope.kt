package com.mashup.telltostar.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class Horoscope(
    @SerializedName("id")
    val id: Int,
    @SerializedName("timeZone")
    val timeZone: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("stylist")
    val stylist: String,
    @SerializedName("numeral")
    val numeral: String,
    @SerializedName("word")
    val word: String,
    @SerializedName("exercise")
    val exercise: String,
    @SerializedName("constellation")
    val constellation: String
)