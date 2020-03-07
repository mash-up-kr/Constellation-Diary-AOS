package com.mashup.telltostar.ui.diary

import android.annotation.SuppressLint
import com.mashup.telltostar.data.exception.Exception
import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.HoroscopeRepository
import com.mashup.telltostar.data.source.UserRepository
import com.mashup.telltostar.data.source.remote.response.Diary
import com.mashup.telltostar.util.PrefUtil
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class DiaryEditPresenter(
    private val view: DiaryEditContract.View,
    private val diaryRepository: DiaryDataRepository,
    private val horoscopeRepository: HoroscopeRepository,
    private val userRepository: UserRepository,
    private val compositeDisposable: CompositeDisposable,
    private val diaryId: Int
) : DiaryEditContract.Presenter {

    private lateinit var diary: Diary
    private var todayHoroscopeId: Int = -1

    override fun loadDiary() {
        if (diaryId > 0) {
            diaryRepository.get(diaryId)
                .subscribe({
                    view.showDiary(it)
                    diary = it
                    Timber.d("diary : $diary")
                }) {
                    onError(it) { loadDiary() }
                }.also {
                    compositeDisposable.add(it)
                }
        }
    }

    override fun loadHoroscope() {
        val constellation = PrefUtil.get(PrefUtil.CONSTELLATION, "")

        horoscopeRepository.get(constellation)
            .subscribe({
                todayHoroscopeId = it.id
                Timber.d("todayHoroscopeId : $todayHoroscopeId")
            }) {
                onError(it) { loadDiary() }
            }.also {
                compositeDisposable.add(it)
            }
    }

    override fun loadHoroscopeDialog(type: DiaryEditActivity.DiaryType) {
        when (type) {
            DiaryEditActivity.DiaryType.WRITE -> {
                if (todayHoroscopeId > 0) view.showHoroscopeDialog(todayHoroscopeId)
            }
            DiaryEditActivity.DiaryType.EDIT -> {
                if (diary.horoscopeId > 0) view.showHoroscopeDialog(diary.horoscopeId)
            }
        }
    }

    override fun done(type: DiaryEditActivity.DiaryType, title: String, contents: String) {
        when (type) {
            DiaryEditActivity.DiaryType.WRITE -> {
                //insert
                writeDiary(todayHoroscopeId, title, contents)

            }
            DiaryEditActivity.DiaryType.EDIT -> {
                //update
                editDiary(diary.horoscopeId, title, contents)
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
                view.finishWithResultOk(title)
            }) {
                onError(it) { writeDiary(horoscopeId, title, contents) }
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
                view.finishWithResultOk(title)
            }) {
                onError(it) { editDiary(horoscopeId, title, contents) }
            }.also {
                compositeDisposable.add(it)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun onError(throwable: Throwable, func: () -> Unit) {
        if (throwable is Exception.AuthenticationException) {
            userRepository.refreshToken()
                .subscribe({
                    func.invoke()
                }) {
                    view.showToast(it.message)
                }
        } else {
            view.showToast(throwable.message)
        }
    }

}