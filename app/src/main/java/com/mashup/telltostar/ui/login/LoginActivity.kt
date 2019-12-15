package com.mashup.telltostar.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bottom_sheet_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun onClick(view: View) {
        when (view) {
            loginSingUpButton -> {
                login_sign_up_bottom_sheet.visibility = View.VISIBLE
            }
            startStarStarDiaryButton -> {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                finish()
            }
        }
    }
}
