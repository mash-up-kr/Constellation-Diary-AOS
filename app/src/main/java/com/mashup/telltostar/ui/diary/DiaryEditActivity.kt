package com.mashup.telltostar.ui.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Constellation
import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.repository.DiaryRepository
import com.mashup.telltostar.util.TimeUtil
import kotlinx.android.synthetic.main.activity_diary_edit.*
import org.jetbrains.anko.toast
import timber.log.Timber

class DiaryEditActivity : AppCompatActivity() {

    private val diaryRepository by lazy {
        DiaryRepository(
            Injection.provideDiaryRepository(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_edit)

        val diary: Diary? = intent?.getParcelableExtra(EXTRA_DIARY)
        Timber.d("diary : $diary")

        initButton()

    }

    private fun initButton() {
        tvDiaryEditDone.setOnClickListener {

            val title = etDiaryEditTitle.text.toString()
            val contents = etDiaryEditContents.text.toString()

            if (title.isEmpty() || contents.isEmpty()) {
                toast("내용을 입력해 주세요")
            } else {

                val diary = Diary(
                    title = title,
                    contents = contents,
                    date = TimeUtil.getCurrentUnixTime(),
                    constellation = Constellation("황소자리")
                )

                diaryRepository.insert(diary).subscribe({
                    setResult(Activity.RESULT_OK,
                        Intent().apply {
                            putExtra(EXTRA_DIARY, diary)
                        })
                    finish()
                }) {
                    toast("error")
                    Timber.e(it)
                }
            }
        }
    }

    companion object {

        const val EXTRA_DIARY = "diary"

        fun startDiaryEditActivity(activity: Activity, requestCode: Int, diary: Diary) {
            activity.startActivityForResult(
                Intent(activity, DiaryEditActivity::class.java).apply {
                    putExtra(EXTRA_DIARY, diary)
                }
                , requestCode
            )
        }

        fun startDiaryWriteActivity(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(
                Intent(activity, DiaryEditActivity::class.java)
                , requestCode
            )
        }
    }
}
