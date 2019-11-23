package com.mashup.telltostar.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import kotlinx.android.synthetic.main.main_contents.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initLayout()

    }

    private fun initLayout() {
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            Timber.d("action bar")

            it.setDisplayShowCustomEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        BottomSheetBehavior.from(llBottomSheetView)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            drawer_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            drawer_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        }
                        else -> {

                        }
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                drawer_container.openDrawer(Gravity.LEFT)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
