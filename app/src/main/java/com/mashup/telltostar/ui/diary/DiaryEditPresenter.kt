package com.mashup.telltostar.ui.diary

import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DiaryEditPresenter(
    private val view: DiaryEditContract.View,
    private val diaryRepository: DiaryDataRepository,
    private val compositeDisposable: CompositeDisposable,
    private val horoscopeId: Int,
    private val diaryId: Int
) : DiaryEditContract.Presenter {

    private lateinit var diary: Diary

    override fun loadDiary() {
        if (diaryId > 0) {
            diaryRepository.get(diaryId)
                .subscribe({
                    view.showDiary(it)
                    this.diary = it
                }) {
                    view.showToast(it.message)
                }.also {
                    compositeDisposable.add(it)
                }
        }
    }

    override fun done(type: DiaryEditActivity.DiaryType, title: String, contents: String) {
        when (type) {
            DiaryEditActivity.DiaryType.WRITE -> {
                //insert
                writeDiary(horoscopeId, title, contents)

            }
            DiaryEditActivity.DiaryType.EDIT -> {
                //update
                editDiary(horoscopeId, title, contents)
            }
        }
    }

    private fun writeDiary(horoscopeId: Int, title: String, contents: String) {
        if (horoscopeId > 0) {
            diaryRepository.insert(
                horoscopeId = horoscopeId,
                title = title,
                content = contents
            ).subscribe({
                view.finishWithResultOk()
            }) {
                view.showToast(it.message)
                Timber.e(it)
            }.also {
                compositeDisposable.add(it)
            }
        }
    }

    private fun editDiary(horoscopeId: Int, title: String, contents: String) {
        if (::diary.isInitialized) {
            diaryRepository.update(
                id = diary.id,
                horoscopeId = horoscopeId,
                title = title,
                content = contents
            ).subscribe({
                view.finishWithResultOk()
            }) {
                view.showToast(it.message)
                Timber.e(it)
            }.also {
                compositeDisposable.add(it)
            }
        }
    }

}