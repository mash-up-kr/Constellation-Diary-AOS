package com.mashup.telltostar.ui.splash

import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.exception.Exception
import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.util.PrefUtil
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hclee on 2020-02-15.
 */

@Singleton
class TokenCheckViewModel @Inject constructor() {
    val shouldStartMain = MutableLiveData<Boolean>()
    val shouldStartLogin = MutableLiveData<Boolean>()
    private val mCompositeDisposable = CompositeDisposable()

    private fun getDailyQuestionsSingle(authenticationToken: String) =
        ApiProvider
            .provideDailyApi()
            .getDailyQuestions("Bearer $authenticationToken", TimeUtil.getUTCDate())
            .composeError()

    private fun getNewAuthenticationTokenSingle(refreshToken: String) =
        ApiProvider
            .provideUserApi()
            .tokens("Bearer $refreshToken")
            .composeError()

    fun requestAuthenticationTokenAvailability() {
        with(PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")) {
            if (this.isNotEmpty()) {
                mCompositeDisposable.add(
                    getDailyQuestionsSingle(this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                shouldStartMain.value = true
                            }, {
                                if (it is Exception.AuthenticationException) {
                                    requestRefreshTokenAvailability()
                                } else {
                                    it.printStackTrace()
                                    shouldStartLogin.value = true
                                }
                            }
                        )
                )
            } else {
                requestRefreshTokenAvailability()
            }
        }
    }

    private fun requestRefreshTokenAvailability() {
        with(PrefUtil.get(PrefUtil.REFRESH_TOKEN, "")) {
            if (this.isNotEmpty()) {
                mCompositeDisposable.add(
                    getNewAuthenticationTokenSingle(this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                PrefUtil.put(PrefUtil.AUTHENTICATION_TOKEN, it.authenticationToken)
                                shouldStartMain.value = true
                            }, {
                                shouldStartLogin.value = true
                                it.printStackTrace()
                            }
                        )
                )
            } else {
                shouldStartLogin.value = true
            }
        }
    }

    fun clearDisposable() {
        mCompositeDisposable.clear()
    }
}