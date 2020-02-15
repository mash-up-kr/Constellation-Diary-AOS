package com.mashup.telltostar.ui.diarylist;

import com.mashup.telltostar.data.source.remote.response.SimpleDiary

data class DataCalendar(
    var stamp : Boolean,
    var select : Boolean,
    var diary : SimpleDiary
)