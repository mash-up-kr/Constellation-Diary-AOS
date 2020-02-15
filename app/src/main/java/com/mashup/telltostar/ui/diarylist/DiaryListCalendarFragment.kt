package com.mashup.telltostar.ui.diarylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mashup.telltostar.R
import com.mashup.telltostar.data.source.remote.response.SimpleDiary
import com.mashup.telltostar.ui.login.PasswordFindFragment


class DiaryListCalendarFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val diaryView = inflater.inflate(R.layout.fragment_diary_list, container, false)


        return diaryView
    }

    fun bind(item : SimpleDiary,day : Int){

    }
}