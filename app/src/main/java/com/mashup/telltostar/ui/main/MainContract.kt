package com.mashup.telltostar.ui.main

import com.mashup.telltostar.data.source.remote.response.Horoscope
import com.mashup.telltostar.ui.base.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

        fun showDiaryTitle(title: String)

        fun showQuestionTitle(title: String)

        fun showHoroscope(horoscope: Horoscope)

        fun showToast(message: String?)

        fun showEditDiary(diaryId: Int, horoscopeId: Int)

        fun showWriteDiary(horoscopeId: Int)

        fun showStarList()

    }

    interface Presenter {

        fun loadDailyQuestion()

        fun loadHoroscope()

        fun editDiary()
    }
}