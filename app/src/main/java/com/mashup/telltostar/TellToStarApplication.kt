package com.mashup.telltostar

import android.app.Application
import com.google.firebase.FirebaseApp
import com.mashup.telltostar.util.PrefUtil
import timber.log.Timber

class TellToStarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initPref()
        setUpTimber()
        initFirebase()
    }

    private fun initPref() {
        PrefUtil.init(this)
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(applicationContext)
    }
}