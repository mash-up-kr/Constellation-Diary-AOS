package com.mashup.telltostar.ui.main

import com.mashup.telltostar.data.exception.Exception
import com.mashup.telltostar.data.source.DailyQuestionRepository
import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.data.source.UserRepository
import com.mashup.telltostar.util.PrefUtil
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainPresenter(
    private val view: MainContract.View,
    private val dailyRepository: DailyQuestionRepository,
    private val horoscopeRepository: HoroscopeRepository,
    private val userRepository: UserRepository,
    private val compositeDisposable: CompositeDisposable
) : MainContract.Presenter {

    private var diaryId = -1

    override fun loadDailyQuestion() {
        dailyRepository.get().subscribe({
            if (it.existDiary) {
                view.showDiaryTitle(it.question)
                diaryId = it.diaryId
            } else {
                view.showQuestionTitle(it.question)
            }
        }) {
            if (it is Exception.AuthenticationException) {
                userRepository.refreshToken()
                    .subscribe({
                        loadDailyQuestion()
                    }) {
                        view.showToast(it.message)
                    }
            } else {
                view.showToast(it.message)
            }
        }.also {
            compositeDisposable.add(it)
        }
    }

    override fun loadHoroscope() {
        val constellation = PrefUtil.get(PrefUtil.CONSTELLATION, "")

        horoscopeRepository.get(constellation)
            .subscribe({
                Timber.d("$it")
                view.showHoroscope(it)
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
            view.showWriteDiary()
        }
    }
}