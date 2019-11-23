package com.mashup.telltostar.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.WindowManager
import com.mashup.telltostar.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_contents.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val statusHeight by lazy {
        resources.getDimensionPixelSize(
            resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initLayout()
        initBottomSheet()

    }

    private fun initLayout() {
        with(appBarLayout) {
            setPadding(
                paddingLeft,
                statusHeight,
                paddingRight,
                paddingBottom
            )
        }

        setSupportActionBar(toolbar)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        supportActionBar?.let {
            Timber.d("action bar")

            it.setDisplayShowCustomEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
    }

    private fun initBottomSheet() {
        with(cdlMainContentsBottomSheetParent) {
            setPadding(
                paddingLeft,
                statusHeight,
                paddingRight,
                paddingBottom
            )
        }
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
