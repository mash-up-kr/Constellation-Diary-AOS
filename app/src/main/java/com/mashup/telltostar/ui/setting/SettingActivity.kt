package com.mashup.telltostar.ui.setting

import android.app.TimePickerDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.dialog.TimePickerBottomSheet
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.toast
import timber.log.Timber

class SettingActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initButton()
        initSwitch()

        //TODO 초기 설정 값 받아오기
        tvSettingHoroscopePush.text = "오전 08:00"
        tvSettingQuestionPush.text = "오후 10:00"

        hideHoroscopePush()
        hideQuestionPush()
    }

    private fun initButton() {
        ivSettingClose.setOnClickListener {
            onBackPressed()
        }
        btnSettingHoroscope.setOnClickListener {
            val timePicker = TimePickerBottomSheet()
            timePicker.show(supportFragmentManager, timePicker.tag)
        }
        btnSettingQuestion.setOnClickListener {
            val timePicker = TimePickerBottomSheet()
            timePicker.show(supportFragmentManager, timePicker.tag)
        }

        rlSettingLogout.setOnClickListener {
            toast("로그아웃")
        }
        rlSettingFeedback.setOnClickListener {
            toast("피드백 주기")
        }
        rlSettingDeveloper.setOnClickListener {
            toast("개발자 정보")
        }
        rlSettingAppVersion.setOnClickListener {
            toast("버전 확인")
        }
    }

    private fun initSwitch() {
        swSettingHoroscope.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showHoroscopePush()
            } else {
                hideHoroscopePush()
            }
        }

        swSettingQuestion.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showQuestionPush()
            } else {
                hideQuestionPush()
            }
        }
    }

    private fun showHoroscopePush() {
        btnSettingHoroscope.isEnabled = true
        tvSettingHoroscopeTime.setTextColor(ContextCompat.getColor(this, R.color.black_two))
        tvSettingHoroscopePush.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.dark_sky_blue_three
            )
        )
        ivSettingHoroscopePush.setColorFilter(
            ContextCompat.getColor(this, R.color.dark_sky_blue_three), PorterDuff.Mode.SRC_IN
        )
    }

    private fun hideHoroscopePush() {
        btnSettingHoroscope.isEnabled = false
        tvSettingHoroscopeTime.setTextColor(ContextCompat.getColor(this, R.color.brownish_grey_two))
        tvSettingHoroscopePush.setTextColor(ContextCompat.getColor(this, R.color.brownish_grey_two))
        ivSettingHoroscopePush.setColorFilter(
            ContextCompat.getColor(this, R.color.brownish_grey_two), PorterDuff.Mode.SRC_IN
        )
    }

    private fun showQuestionPush() {
        btnSettingQuestion.isEnabled = true
        tvSettingQuestionTime.setTextColor(ContextCompat.getColor(this, R.color.black_two))
        tvSettingQuestionPush.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.dark_sky_blue_three
            )
        )
        ivSettingQuestionPush.setColorFilter(
            ContextCompat.getColor(this, R.color.dark_sky_blue_three), PorterDuff.Mode.SRC_IN
        )
    }

    private fun hideQuestionPush() {
        btnSettingQuestion.isEnabled = false
        tvSettingQuestionTime.setTextColor(ContextCompat.getColor(this, R.color.brownish_grey_two))
        tvSettingQuestionPush.setTextColor(ContextCompat.getColor(this, R.color.brownish_grey_two))
        ivSettingQuestionPush.setColorFilter(
            ContextCompat.getColor(this, R.color.brownish_grey_two), PorterDuff.Mode.SRC_IN
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        toast("hourOfDay : $hourOfDay , minute : $minute")
        Timber.d("hourOfDay : $hourOfDay , minute : $minute")
    }
}
