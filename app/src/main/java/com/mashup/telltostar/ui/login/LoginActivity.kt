package com.mashup.telltostar.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_login.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        drawerLayoutLogin.addDrawerListener(DRAWER_LISTENER)
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
