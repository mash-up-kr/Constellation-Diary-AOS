package com.mashup.telltostar.ui.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.repository.DiaryRepoImpl
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_diary_edit.*
import org.jetbrains.anko.toast
import timber.log.Timber

class DiaryEditActivity : AppCompatActivity() {

    private val diaryRepository by lazy {
        DiaryRepoImpl(
            Injection.provideDiaryRepo(this)
        )
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_edit)

        initButton()
        initIntent()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun initButton() {
        tvDiaryEditDone.setOnClickListener {

            val title = etDiaryEditTitle.text.toString()
            val contents = etDiaryEditContents.text.toString()

            if (title.isEmpty() || contents.isEmpty()) {
                toast("내용을 입력해 주세요")
            } else {
                intent?.run {
                    //TODO insert, update 구분 하기
                    val horoscopeId = getIntExtra(EXTRA_HOROSCOPE_ID, -1)
                    if (horoscopeId > 0) {
                        diaryRepository.insert(
                            horoscopeId = horoscopeId,
                            title = title,
                            content = contents
                        ).subscribe({
                            setResult(Activity.RESULT_OK)
                            finish()
                        }) {
                            toast("error")
                            Timber.e(it)
                        }
                    }
                }
            }
        }
    }

    private fun initIntent() {

        intent?.run {
            val diaryId = getIntExtra(EXTRA_DIARY_ID, -1)
            val horoscopeId = getIntExtra(EXTRA_HOROSCOPE_ID, -1)
            Timber.d("diaryId : $diaryId , horoscopeId : $horoscopeId")

            if (diaryId > 0) {
                diaryRepository.get(diaryId)
                    .subscribe({ diary ->
                        Timber.d("diary : $diary")

                        etDiaryEditTitle.setText(diary.title)
                        etDiaryEditContents.setText(diary.content)

                    }) {
                        Timber.e(it)
                        toast(it.message.toString())
                    }.also {
                        compositeDisposable.add(it)
                    }
            }
        }
    }

    companion object {

        const val EXTRA_DIARY_ID = "diary_id"
        const val EXTRA_HOROSCOPE_ID = "horoscope_id"

        fun startDiaryEditActivity(activity: Activity, requestCode: Int, diaryId: Int) {
            activity.startActivityForResult(
                Intent(activity, DiaryEditActivity::class.java).apply {
                    putExtra(EXTRA_DIARY_ID, diaryId)
                }
                , requestCode
            )
        }

        fun startDiaryWriteActivity(activity: Activity, requestCode: Int, horoscopeId: Int) {
            activity.startActivityForResult(
                Intent(activity, DiaryEditActivity::class.java).apply {
                    putExtra(EXTRA_HOROSCOPE_ID, horoscopeId)
                }
                , requestCode
            )
        }
    }
}
