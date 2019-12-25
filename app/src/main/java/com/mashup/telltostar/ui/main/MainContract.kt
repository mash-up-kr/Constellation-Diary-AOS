package com.mashup.telltostar.ui.main

import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.ui.base.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

        fun showDiaryTitle(title: String)

        fun showQuestionTitle()

        fun showEditDiary(diary: Diary)

        fun showWriteDiary()

        fun showToast(message: String?)

    }

    interface Presenter {

        fun loadTodayDiary()

        fun startDiary()

        fun changeDiary(diary: Diary)
    }
}