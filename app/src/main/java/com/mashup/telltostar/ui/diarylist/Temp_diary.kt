package com.mashup.telltostar.ui.diarylist

import com.mashup.telltostar.data.source.remote.response.SimpleDiary

data class Temp_diary (
    val diary : SimpleDiary,
    var isVisible : Boolean,
    var isChecked : Boolean
)