package com.mashup.telltostar.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.ui.main.MainActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private lateinit var mDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    private fun startLoginActivity() {
        startActivity(LoginActivity::class.java)
    }

    private fun startMainActivity() {
        startActivity(MainActivity::class.java)
    }

    private fun <T> startActivity(activity: Class<T>) {
        mDisposable = Single.just(activity)
            .subscribeOn(Schedulers.io())
            .delay(2500L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    startActivity(Intent(this, it))
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                    finish()
                }, {
                    it.printStackTrace()
                }
            )
    }

    override fun onDestroy() {
        mDisposable.dispose()

        super.onDestroy()
    }
}
