package com.mashup.telltostar.ui.main

import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.repository.DiaryRepository
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainPresenter(
    private val view: MainContract.View,
    private val diaryRepository: DiaryRepository,
    private val compositeDisposable: CompositeDisposable
) : MainContract.Presenter {

    private var diary: Diary? = null

    override fun loadTodayDiary() {

        diaryRepository.getTodayDiaries().subscribe({
            Timber.d("current diaries : $it")
            if (it.isNotEmpty()) {
                val todayDiary = it[0]
                this.diary = todayDiary
                view.showDiaryTitle(todayDiary.title)
            } else {
                view.showQuestionTitle()
            }
        }) {
            Timber.e(it)
            view.showToast(it.message)
        }.also {
            compositeDisposable.add(it)
        }
    }

    override fun startDiary() {
        if (diary != null) {
            view.showEditDiary(diary!!)
        } else {
            view.showWriteDiary()

        }
    }

    override fun changeDiary(diary: Diary) {
        this.diary = diary
        view.showDiaryTitle(diary.title)
    }
}