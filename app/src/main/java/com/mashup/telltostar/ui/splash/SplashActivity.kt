package com.mashup.telltostar.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mashup.telltostar.R
import com.mashup.telltostar.ui.login.LoginActivity
import com.mashup.telltostar.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val TIME_LIMIT = 2L
    }

    private val mCompositeDisposable = CompositeDisposable()
    private var isTimeCountFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setViewModelObserver()
    }

    override fun onResume() {
        super.onResume()

        startCount()
        TokenCheckViewModel.requestAuthenticationTokenAvailability()
    }

    private fun setViewModelObserver() {
        with(TokenCheckViewModel) {
            shouldStartLogin.observe(this@SplashActivity, Observer {
                if (shouldStartLoginActivity()) {
                    startLoginActivity()
                }
            })
            shouldStartMain.observe(this@SplashActivity, Observer {
                if (shouldStartMainActivity()) {
                    startMainActivity()
                }
            })
        }
    }

    private fun startCount() {
        mCompositeDisposable.add(
            Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .take(TIME_LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it == (TIME_LIMIT - 1)) {
                        isTimeCountFinish = true
                    }

                    if (shouldStartLoginActivity()) {
                        startLoginActivity()
                    } else if (shouldStartMainActivity()) {
                        startMainActivity()
                    }
                },
                    { },
                    { })
        )
    }

    private fun shouldStartLoginActivity() =
        TokenCheckViewModel.shouldStartLogin.value?.let {
            it && isTimeCountFinish
        } ?: false

    private fun shouldStartMainActivity() =
        TokenCheckViewModel.shouldStartMain.value?.let {
            it && isTimeCountFinish
        } ?: false

    private fun startLoginActivity() {
        startActivity(LoginActivity::class.java)
    }

    private fun startMainActivity() {
        startActivity(MainActivity::class.java)
    }

    private fun <T> startActivity(activity: Class<T>) {
        startActivity(Intent(this, activity))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        finish()
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        TokenCheckViewModel.clearDisposable()

        super.onDestroy()
    }
}
