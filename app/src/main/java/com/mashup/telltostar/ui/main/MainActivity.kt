package com.mashup.telltostar.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mashup.telltostar.R
import com.readystatesoftware.systembartint.SystemBarTintManager

class MainActivity : AppCompatActivity() {
    private val mTintManager: SystemBarTintManager by lazy {
        SystemBarTintManager(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(mTintManager) {
            isStatusBarTintEnabled = true
            setNavigationBarTintEnabled(true)
            setTintColor(android.R.color.holo_green_dark)
//            setNavigationBarTintResource()
        }
    }
}
