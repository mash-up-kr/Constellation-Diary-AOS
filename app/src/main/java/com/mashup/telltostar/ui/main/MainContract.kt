package com.mashup.telltostar.ui.main

import com.mashup.telltostar.ui.base.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

        fun showTitle(title: String)

        fun showDescription()

    }

    interface Presenter {

        fun startDiaryEdit()

    }
}