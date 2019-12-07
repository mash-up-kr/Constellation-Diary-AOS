package com.mashup.telltostar.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.diary.DiaryEditActivity
import com.mashup.telltostar.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import kotlinx.android.synthetic.main.main_contents.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_DIARY_EDIT = 0x001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initLayout()

    }

    private fun initLayout() {
        setSupportActionBar(toolbarMain as Toolbar)

        supportActionBar?.let {
            Timber.d("action bar")

            it.setDisplayShowCustomEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }

        dateTextView.text = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA).apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }.format(Date())

        BottomSheetBehavior.from(llBottomSheetView)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            drawer_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                        }
                        else -> {
                            drawer_container.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        }
                    }
                }
            })
    }

    fun onClick(view: View) {
        when (view) {
            editDiaryImageView -> {
//                startActivityForResult(
//                    Intent(this@MainActivity, DiaryEditActivity::class.java),
//                    REQUEST_DIARY_EDIT
//                )
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_DIARY_EDIT -> {

            }
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
