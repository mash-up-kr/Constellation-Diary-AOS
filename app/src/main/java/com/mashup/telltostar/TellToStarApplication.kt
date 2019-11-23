package com.mashup.telltostar

import android.app.Application
import com.mashup.telltostar.util.PrefUtil
import timber.log.Timber

class TellToStarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initPref()
        setUpTimber()
    }

    private fun initPref() {
        PrefUtil.init(this)
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}