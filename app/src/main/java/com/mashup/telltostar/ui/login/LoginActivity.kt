package com.mashup.telltostar.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.view.*

class LoginActivity : AppCompatActivity() {
    private val mIntroFragment by lazy {
        IntroFragment()
    }
    private val mMyConstellationFragment by lazy {
        MyConstellationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initToolbar()
        replaceFragment(mIntroFragment)
    }

    private fun initToolbar() {
        setSupportActionBar((toolbarLogin as Toolbar).apply {
            toolbarTextView.text = getString(R.string.title_login_activity)
        })

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(loginFrame.id, fragment).commit()
    }
}
