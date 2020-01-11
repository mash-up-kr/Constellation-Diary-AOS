package com.mashup.telltostar.ui.setting

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.*

class SettingActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initButton()

        tvSettingAmTime.text = "09:00"
    }

    private fun initButton() {
        ivSettingClose.setOnClickListener {
            onBackPressed()
        }
        tvSettingAmPush.setOnClickListener {

            val cldr = Calendar.getInstance()
            val hour = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes = cldr.get(Calendar.MINUTE)

            val dialog = TimePickerDialog(
                this, this, hour, minutes, false
            )
            dialog.show()
        }

        rlSettingLogout.setOnClickListener {
            toast("로그아웃")
        }

        rlSettingFeedback.setOnClickListener {
            toast("피드백 주기")
        }
        tvSettingVersion.setOnClickListener {
            toast("버전 확인")
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        toast("hourOfDay : $hourOfDay , minute : $minute")
        Timber.d("hourOfDay : $hourOfDay , minute : $minute")
    }
}
