package com.mashup.telltostar.ui.diarylist

data class Temp_diary (
    val id : Int,
    val title:String,
    val date : String,
    var isVisible : Boolean,
    var isChecked : Boolean
)