package com.mashup.telltostar.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.myconstellation.MyConstellationActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bottom_sheet_login.*

class LoginActivity : AppCompatActivity() {
    private val mBottomSheetBehavior by lazy {
        BottomSheetBehavior.from(login_sign_up_bottom_sheet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initBottomSheetView()
    }

    private fun initBottomSheetView() {
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= -1f) {
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
    }

    fun onClick(view: View) {
        when (view) {
            loginSingUpButton -> {
                with(login_sign_up_bottom_sheet) {
                    visibility = View.VISIBLE
                    mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            startStarStarDiaryButton -> {
                startActivity(Intent(
                    this@LoginActivity, MyConstellationActivity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                finish()
            }
        }
    }
}
