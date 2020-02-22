package com.mashup.telltostar.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.bottom_sheet_time_picker.*

class TimePickerBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTimePicker()
        initButton()
    }

    private lateinit var type: TimePickerType

    private fun initTimePicker() {
        arguments?.run {
            type = getSerializable(KEY_TYPE) as TimePickerType
        }
    }

    private fun initButton() {

        tvTimePickerCancel.setOnClickListener {
            dismiss()
        }

        //오전 12시 -> hour : 0
        //오후 12시 -> hour : 12
        tvTimePickerOk.setOnClickListener {

            val hourOfDay: Int
            val minute: Int

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hourOfDay = timePicker.hour
                minute = timePicker.minute
            } else {
                hourOfDay = timePicker.currentHour
                minute = timePicker.currentMinute
            }

            val hour = if (hourOfDay == 0) {
                (hourOfDay + 12)
            } else if (hourOfDay == 12) {
                (hourOfDay + 12)
            } else {
                hourOfDay
            }

            val time = "$hour:$minute"

            when (type) {
                TimePickerType.HOROSCOPE -> {
                    (requireActivity() as SettingActivity).setHoroscopeTime(time)
                }
                TimePickerType.QUESTION -> {
                    (requireActivity() as SettingActivity).setQuestionTime(time)
                }

            }

            dismiss()
        }
    }

    enum class TimePickerType {
        HOROSCOPE, QUESTION
    }

    companion object {

        private const val KEY_TYPE = "type"

        fun newInstance(type: TimePickerType): TimePickerBottomSheet {
            return TimePickerBottomSheet().apply {
                arguments = bundleOf(Pair(KEY_TYPE, type))
            }
        }
    }
}