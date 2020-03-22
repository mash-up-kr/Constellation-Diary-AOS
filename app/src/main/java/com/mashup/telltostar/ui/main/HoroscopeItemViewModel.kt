package com.mashup.telltostar.ui.main

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.mashup.telltostar.data.source.remote.response.Horoscope

class HoroscopeItemViewModel(
    val horoscope: Horoscope
) {

    fun onClickStyle(view: View, item: String) {
        showItemDialog(view.context, "소품", item)
    }

    fun onClickNumber() {

    }

    fun onClickExercise() {

    }

    fun onClickWord() {

    }

    private fun showItemDialog(context: Context, keyword: String, item: String) {
        val alertDialogBuilder = AlertDialog.Builder(context)

        //alertDialogBuilder.setTitle("확인해주세요")
        alertDialogBuilder.setMessage("당신의 행운의 ${keyword}은 ${item}입니다.")
            .setCancelable(true)
        //.setPositiveButton("확인") { _, _ -> }

        //.setNegativeButton("취소") { dialog, which -> }

        // 다이얼로그 생성
        val alertDialog = alertDialogBuilder.create()

        // 다이얼로그 보여주기
        alertDialog.show()
    }

}