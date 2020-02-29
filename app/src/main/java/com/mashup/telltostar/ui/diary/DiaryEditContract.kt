package com.mashup.telltostar.ui.diary

import com.mashup.telltostar.data.source.remote.response.Diary
import com.mashup.telltostar.ui.base.BaseView

interface DiaryEditContract {

    interface View : BaseView<Presenter> {

        fun showDiary(diary: Diary)

        fun showHoroscopeDialog(horoscopeId: Int)

        fun showToast(message: String?)

        fun finishWithResultOk(title: String)

        fun showKeyboard()

        fun hideKeyboard()

    }

    interface Presenter {

        fun loadDiary()

        fun loadHoroscopeDialog(type: DiaryEditActivity.DiaryType)

        fun done(type: DiaryEditActivity.DiaryType, title: String, contents: String)

    }
}