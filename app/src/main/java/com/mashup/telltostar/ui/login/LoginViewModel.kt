package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqSignInDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoErrorDto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

/**
 * Created by hclee on 2020-01-12.
 */

class LoginViewModel {
    val isInputIdWarningTextViewVisible = MutableLiveData<Boolean>()
    val isInputPasswordWarningTextViewVisible = MutableLiveData<Boolean>()
    val isLoginErrorButtonVisible = MutableLiveData<Boolean>(false)
    val isLoggedIn = MutableLiveData<Boolean>(false)
    val isInputPasswordVisible = MutableLiveData<Boolean>(false)
    var mAuthenticationToken = ""
    var mRefreshToken = ""
    var constellation = ""
    var horoscopeAlarmFlag = true
    var questionAlarmFlag = true
    var horoscopeAlarmTime = ""
    var questionAlarmTime = ""

    private val mCompositeData by lazy {
        CompositeDisposable()
    }

    fun requestLogin(timeZone: String, fcmToken: String, id: String, password: String) {
        isInputIdWarningTextViewVisible.postValue(id.isEmpty())
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())

        if (id.isNotEmpty() && password.isNotEmpty()) {
            mCompositeData.add(
                ApiProvider
                    .provideUserApi()
                    .signIn(
                        timeZone,
                        ReqSignInDto(
                            fcmToken,
                            id,
                            password
                        )
                    )
                    .onErrorReturn {
                        getConvertedErrorData(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it is ResUserInfoErrorDto) {
                            when (it.error.code) {
                                4104, 4105 -> {
                                    showThenHideLoginErrorMessage()
                                }
                            }
                        } else if (it is ResUserInfoDto) {
                            mAuthenticationToken = it.tokens.authenticationToken
                            mRefreshToken = it.tokens.refreshToken

                            constellation = it.user.constellation

                            horoscopeAlarmFlag = it.user.horoscopeAlarmFlag
                            horoscopeAlarmTime = it.user.horoscopeTime

                            questionAlarmFlag = it.user.questionAlarmFlag
                            questionAlarmTime = it.user.questionTime

                            isLoggedIn.value = true
                        }
                    }, {
                        isLoggedIn.value = false
                    })
            )
        }
    }

    private fun getConvertedErrorData(throwable: Throwable) =
        Gson().fromJson(
            (throwable as HttpException).response().errorBody()?.string(),
            ResUserInfoErrorDto::class.java
        )

    private fun showThenHideLoginErrorMessage() {
        mCompositeData.add(
            Observable
                .interval(0, 1500L, TimeUnit.MILLISECONDS)
                .take(2)
                .flatMap {
                    if (it < 1) {
                        Observable.just(true)
                    } else {
                        Observable.just(false)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isLoginErrorButtonVisible.value = it
                }, {

                }, {

                })
        )
    }

    fun requestTogglePasswordVisibility() {
        isInputPasswordVisible.value = isInputPasswordVisible.value?.not()
    }
}