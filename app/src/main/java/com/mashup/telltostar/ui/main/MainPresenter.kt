package com.mashup.telltostar.ui.main

import com.mashup.telltostar.data.source.DailyQuestionRepository
import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.util.PrefUtil
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainPresenter(
    private val view: MainContract.View,
    private val dailyRepository: DailyQuestionRepository,
    private val horoscopeRepository: HoroscopeRepository,
    private val compositeDisposable: CompositeDisposable
) : MainContract.Presenter {

    private var diaryId = -1
    private var horoscopeId = -1

    override fun loadDailyQuestion() {

        dailyRepository.get().subscribe({
            if (it.existDiary) {
                view.showDiaryTitle(it.question)
                diaryId = it.diaryId
            } else {
                view.showQuestionTitle(it.question)
            }
        }) {
            Timber.e(it)
            view.showToast(it.message)
        }.also {
            compositeDisposable.add(it)
        }
    }

    override fun loadHoroscope() {

        val constellation = PrefUtil.get(PrefUtil.CONSTELLATION, "")
        Timber.d(constellation)

        horoscopeRepository.get(constellation)
            .subscribe({
                Timber.d("$it")
                view.showHoroscope(it)
                horoscopeId = it.id
            }) {
                Timber.e(it)
                view.showToast(it.message)
            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun editDiary() {
        if (diaryId > 0) {
            view.showEditDiary(diaryId)
        } else {
            view.showWriteDiary(horoscopeId)
        }
    }
}