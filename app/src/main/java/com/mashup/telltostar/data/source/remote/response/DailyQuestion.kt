package com.mashup.telltostar.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class DailyQuestion(
    @SerializedName("existDiary")
    val existDiary: Boolean = false,
    @SerializedName("question")
    val question: String = "당신은 지금 무슨\n생각을 하고 있나요?",
    @SerializedName("diaryId")
    val diaryId: Int = -1
)