package com.mashup.telltostar.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Created by hclee on 2020-01-15.
 */

object VibratorUtil {
    fun vibrate(
        context: Context,
        milliseconds: Long = 30,
        amplitude: Int = -1
    ) {
        with(context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate(VibrationEffect.createOneShot(milliseconds, amplitude), null)
            } else {
                vibrate(milliseconds, null)
            }
        }
    }
}