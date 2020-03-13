package com.mashup.telltostar.eventbus

import io.reactivex.subjects.PublishSubject

object RxEventBusHelper {

    val diaryTitleBus = PublishSubject.create<String>()

    fun sendDiaryTitle(title: String) {
        diaryTitleBus.onNext(title)
    }

}