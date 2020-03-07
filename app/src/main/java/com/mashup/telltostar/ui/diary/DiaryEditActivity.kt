package com.mashup.telltostar.ui.diary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.Diary
import com.mashup.telltostar.eventbus.RxEventBusHelper
import com.mashup.telltostar.ui.dialog.HoroscopeDialog
import com.mashup.telltostar.util.AppUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_diary_edit.*
import org.jetbrains.anko.toast

class DiaryEditActivity : AppCompatActivity(), DiaryEditContract.View {

    override lateinit var presenter: DiaryEditContract.Presenter

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_edit)

        presenter = DiaryEditPresenter(
            view = this,
            diaryRepository = Injection.provideDiaryRepo(this),
            horoscopeRepository = Injection.provideHoroscopeRepo(),
            userRepository = Injection.provideUserRepo(),
            compositeDisposable = compositeDisposable,
            diaryId = intent.getIntExtra(EXTRA_DIARY_ID, -1)
        )

        initButton()
        initIntent()
        initLimitLineTitle()
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
                presenter.done(
                    type = intent.getSerializableExtra(EXTRA_DIARY_TYPE) as DiaryType,
                    title = title,
                    contents = contents
                )
            }
        }

        ivDiaryEditClose.setOnClickListener {
            onBackPressed()
        }

        ivDiaryEditHoroscope.setOnClickListener {
            intent?.run {
                val type = intent.getSerializableExtra(EXTRA_DIARY_TYPE) as DiaryType
                presenter.loadHoroscopeDialog(type)
            }
        }
    }

    private fun initIntent() {
        intent?.run {
            when (getSerializableExtra(EXTRA_DIARY_TYPE) as DiaryType) {
                DiaryType.WRITE -> {
                    presenter.loadHoroscope()
                    tvDiaryEditDate.text = TimeUtil.getKSTDateFromUTCDate(
                        TimeUtil.getUTCDate()
                    )
                    showKeyboard()
                }
                DiaryType.EDIT -> {
                    presenter.loadDiary()
                }
            }
        }
    }

    private fun initLimitLineTitle() {
        etDiaryEditTitle.setOnEditorActionListener { v, _, _ ->
            return@setOnEditorActionListener if (v.lineCount > 1) {
                showToastWithoutOverlap("2줄까지만 작성해 주세요")
                true
            } else {
                false
            }
        }
    }

    private var toast: Toast? = null

    private fun showToastWithoutOverlap(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun showDiary(diary: Diary) {
        etDiaryEditTitle.setText(diary.title)
        etDiaryEditContents.setText(diary.content)
        tvDiaryEditDate.text = TimeUtil.getKSTDateFromUTCDate(diary.date)
    }

    override fun showHoroscopeDialog(horoscopeId: Int) {
        HoroscopeDialog.newInstance(horoscopeId).show(supportFragmentManager, "horoscope")
    }

    override fun showToast(message: String?) {
        if (message != null) {
            toast(message)
        }
    }

    override fun finishWithResultOk(title: String) {
        RxEventBusHelper.sendDiaryTitle(title)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showKeyboard() {
        etDiaryEditTitle.requestFocus()
        AppUtil.showKeyboard(etDiaryEditTitle)
    }

    override fun hideKeyboard() {
        AppUtil.hideKeyboard(this)
    }

    enum class DiaryType {
        WRITE, EDIT
    }

    companion object {

        const val EXTRA_DIARY_TYPE = "diary_type"
        const val EXTRA_DIARY_ID = "diary_id"

        fun startDiaryWriteActivity(activity: Activity, requestCode: Int, horoscopeId: Int) {
            activity.startActivityForResult(
                Intent(activity, DiaryEditActivity::class.java).apply {
                    putExtra(EXTRA_DIARY_TYPE, DiaryType.WRITE)
                }
                , requestCode
            )
        }

        fun startDiaryEditActivity(
            activity: Activity,
            requestCode: Int,
            diaryId: Int
        ) {
            activity.startActivityForResult(
                Intent(activity, DiaryEditActivity::class.java).apply {
                    putExtra(EXTRA_DIARY_TYPE, DiaryType.EDIT)
                    putExtra(EXTRA_DIARY_ID, diaryId)
                }
                , requestCode
            )
        }
    }
}
