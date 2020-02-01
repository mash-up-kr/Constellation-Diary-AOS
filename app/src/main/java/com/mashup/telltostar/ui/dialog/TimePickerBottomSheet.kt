package com.mashup.telltostar.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.bottom_sheet_time_picker.*
import org.jetbrains.anko.support.v4.toast

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

    private fun initTimePicker() {
        with(timePicker) {
            //TODO set init time
        }
    }

    private fun initButton() {

        tvTimePickerCancel.setOnClickListener {
            dismiss()
        }

        tvTimePickerOk.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toast("hour : ${timePicker.hour} , minute : ${timePicker.minute}")
            } else {
                toast("hour : ${timePicker.currentHour} , minute : ${timePicker.currentMinute}")
            }
        }
    }
}