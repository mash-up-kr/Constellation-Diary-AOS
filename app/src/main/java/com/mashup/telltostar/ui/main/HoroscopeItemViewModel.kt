package com.mashup.telltostar.ui.main

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.mashup.telltostar.data.source.remote.response.Horoscope

class HoroscopeItemViewModel(
    val horoscope: Horoscope
) {

    fun onClickStyle(view: View) {
        showItemDialog(view.context)
    }

    fun onClickNumber(view: View) {
        showItemDialog(view.context)
    }

    fun onClickExercise(view: View) {
        showItemDialog(view.context)
    }

    fun onClickWord(view: View) {
        showItemDialog(view.context)
    }

    private fun showItemDialog(context: Context) {

        val alertDialogBuilder = AlertDialog.Builder(context)

        //alertDialogBuilder.setTitle("확인해주세요")
        alertDialogBuilder.setMessage("행운의 키워드는 각각 소품, 숫자, 운동, 단어를 의미합니다")
            .setCancelable(true)
        //.setPositiveButton("확인") { _, _ -> }

        //.setNegativeButton("취소") { dialog, which -> }

        // 다이얼로그 생성
        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 보여주기
        alertDialog.show()
    }

}