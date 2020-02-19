package com.mashup.telltostar.ui.setting

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mashup.telltostar.BuildConfig
import com.mashup.telltostar.R
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.exception.Exception
import com.mashup.telltostar.ui.dialog.TimePickerBottomSheet
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import timber.log.Timber


class SettingActivity : AppCompatActivity() {

    private val userRepository by lazy {
        Injection.provideUserRepo()
    }

    private val userChangeRepository by lazy {
        Injection.provideUserChangeRepo()
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initButton()
        initSwitch()
        initPush()
        initVersion()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun initButton() {
        ivSettingClose.setOnClickListener {
            onBackPressed()
        }
        btnSettingHoroscope.setOnClickListener {
            val timePicker =
                TimePickerBottomSheet.newInstance(TimePickerBottomSheet.TimePickerType.HOROSCOPE)
            timePicker.show(supportFragmentManager, timePicker.tag)
        }
        btnSettingQuestion.setOnClickListener {
            val timePicker =
                TimePickerBottomSheet.newInstance(TimePickerBottomSheet.TimePickerType.QUESTION)
            timePicker.show(supportFragmentManager, timePicker.tag)
        }

        rlSettingLogout.setOnClickListener {
            alert(message = "로그아웃 하실 건가요?") {
                positiveButton("로그하웃") {
                    logout()
                }
                negativeButton("취소") {

                }
            }.show()

        }
        rlSettingFeedback.setOnClickListener {
            feedback()
        }
        rlSettingDeveloper.setOnClickListener {
            alert(
                message = "안드로이드 : 최민정, 이진성, 이해창\n" +
                        "디자인 : 고은이, 이정은, 님궁욱\n" +
                        "서버 : 이동근"
            ) {
                positiveButton("확인") {

                }
            }.show()
        }
        rlSettingAppVersion.setOnClickListener {
            //toast("버전 확인")
        }
    }

    private fun logout() {
        userRepository.logout()
            .subscribe({
                Timber.e("complete logout")
                PrefUtil.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }) {
                if (it is Exception.AuthenticationException) {
                    userRepository.refreshToken()
                        .subscribe({
                            logout()
                        }) {
                            toast(it.message ?: "error")
                        }
                } else {
                    toast(it.message ?: "error")
                }
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun feedback() {
        val email = Intent(Intent.ACTION_SEND).apply {
            type = "plain/Text"
            val address = arrayOf("dlgockd45@gmail.com")
            putExtra(Intent.EXTRA_EMAIL, address)
            putExtra(Intent.EXTRA_SUBJECT, "<" + getString(R.string.app_name) + ">")
            putExtra(
                Intent.EXTRA_TEXT,
                "AppVersion :${BuildConfig.VERSION_NAME}\nDevice : ${Build.MODEL}\nAndroid OS : ${Build.VERSION.SDK_INT}\n\n Content :\n"
            )
        }
        startActivity(email)
    }

    private fun initSwitch() {
        swSettingHoroscope.setOnCheckedChangeListener { _, isChecked ->
            userChangeRepository.setHoroscopeAlarm(isChecked)
                .subscribe({
                    Timber.d("setHoroscopeAlarm : $it")
                    if (isChecked) {
                        showHoroscopePush()
                    } else {
                        hideHoroscopePush()
                    }
                }) {
                    Timber.e(it)
                }.also {
                    compositeDisposable.add(it)
                }
        }

        swSettingQuestion.setOnCheckedChangeListener { _, isChecked ->
            userChangeRepository.setQuestionAlarm(isChecked)
                .subscribe({
                    Timber.d("setQuestionAlarm : $it")
                    if (isChecked) {
                        showQuestionPush()
                    } else {
                        hideQuestionPush()
                    }
                }) {
                    Timber.e(it)
                }.also {
                    compositeDisposable.add(it)
                }
        }
    }

    private fun initPush() {
        val horoscopeFlag = PrefUtil.get(PrefUtil.HOROSCOPE_ALARM_FLAG, true)

        if (horoscopeFlag) {
            swSettingHoroscope.isChecked = true
            showHoroscopePush()
        } else {
            swSettingHoroscope.isChecked = false
            hideHoroscopePush()
        }

        val questionFlag = PrefUtil.get(PrefUtil.QUESTION_ALARM_FLAG, true)

        if (questionFlag) {
            swSettingQuestion.isChecked = true
            showQuestionPush()
        } else {
            swSettingQuestion.isChecked = false
            hideQuestionPush()
        }

        tvSettingHoroscopePush.text = TimeUtil.getAlarmFromTime(
            PrefUtil.get(PrefUtil.HOROSCOPE_TIME, "")
        )
        tvSettingQuestionPush.text = TimeUtil.getAlarmFromTime(
            PrefUtil.get(PrefUtil.QUESTION_TIME, "")
        )
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

    fun setHoroscopeTime(time: String) {
        val alarm = TimeUtil.getAlarmFromTime(time)
        userChangeRepository.setHoroscopeTime(time)
            .subscribe({
                Timber.d("setHoroscopeTime : $it")
                tvSettingHoroscopePush.text = alarm
            }) {
                Timber.e(it)
            }.also {
                compositeDisposable.add(it)
            }
    }

    fun setQuestionTime(time: String) {
        val alarm = TimeUtil.getAlarmFromTime(time)
        userChangeRepository.setQuestionTime(time)
            .subscribe({
                Timber.d("setQuestionTime : $it")
                tvSettingQuestionPush.text = alarm
            }) {
                Timber.e(it)
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun initVersion() {
        tvSettingVersion.text = "ver. ${BuildConfig.VERSION_NAME}"
    }
}
