package com.mashup.telltostar.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.login_contents.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    companion object {
        private val DRAWER_LISTENER = object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                Timber.d("onDrawerStateChanged()")

                when (newState) {
                    DrawerLayout.STATE_IDLE -> {
                        Timber.d("state: idle")
                    }
                    DrawerLayout.STATE_DRAGGING -> {
                        Timber.d("state: dragging")
                    }
                    DrawerLayout.STATE_SETTLING -> {
                        Timber.d("state: settling")
                    }
                }
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                Timber.d("onDrawerSlide()")
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }
        }
    }

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

        drawerLayoutLogin.addDrawerListener(DRAWER_LISTENER)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarLogin as Toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(loginFrame.id, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawerLayoutLogin.openDrawer(GravityCompat.START)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        drawerLayoutLogin.removeDrawerListener(DRAWER_LISTENER)

        super.onDestroy()
    }
}
