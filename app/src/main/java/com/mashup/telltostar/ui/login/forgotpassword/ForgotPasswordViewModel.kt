package com.mashup.telltostar.ui.login.forgotpassword

import android.util.Patterns
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqModifyPasswordDto
import com.mashup.telltostar.data.source.remote.request.ReqValidationFindPasswordNumberDto
import com.mashup.telltostar.data.source.remote.request.ReqFindPasswordNumberDto
import com.mashup.telltostar.data.source.remote.response.ResAuthenticationTokenErrorDto
import com.mashup.telltostar.data.source.remote.response.ResFindPasswordNumberErrorDto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hclee on 2020-01-17.
 */

@Singleton
class ForgotPasswordViewModel @Inject constructor() {
    companion object {
        private const val TIMEOUT = 10 * 60L
    }

    private val mCompositeDisposable by lazy {
        CompositeDisposable()
    }
    val isIdEmptyWarningVisibleObservable = ObservableBoolean(false)
    val isEmailEmptyWarningVisibleObservable = ObservableBoolean(false)
    val isVerificationNumberRequestedObservable = ObservableBoolean(false)
    val isTimeRemainsObservable = ObservableBoolean()
    val mRemainTimeObservable = ObservableField<String>()
    val isVerifiedObservable = ObservableBoolean(false)
    val isLoadingVisibleObservable = ObservableBoolean(false)
    val isPasswordEmptyWarningVisibleObservable = ObservableBoolean(false)
    val isPasswordNotIdenticalWarningVisibleObservable = ObservableBoolean(false)
    val isPasswordInputIdenticalLiveData = MutableLiveData<Boolean>(false)
    var isVerificationTimeoutLiveData = MutableLiveData<Boolean>(false)
    var isVerificationNumberWrongLiveData = MutableLiveData<Boolean>(false)
    var isNonExistentEmailLiveData = MutableLiveData<Boolean>(false)
    private var mVerificationToken: String? = null

    fun requestVerificationNumber(id: String, email: String) {
        isVerificationTimeoutLiveData.value = false
        isVerificationNumberWrongLiveData.value = false
        isNonExistentEmailLiveData.value = false
        isIdEmptyWarningVisibleObservable.set(id.isEmpty())
        isEmailEmptyWarningVisibleObservable.set(email.isEmpty() || isEmailPattern(email).not())

        if (id.isNotEmpty() && email.isNotEmpty()) {
            if (isEmailPattern(email)) {
                mCompositeDisposable.clear()
                isLoadingVisibleObservable.set(true)
                mCompositeDisposable.add(
                    Injection
                        .provideAuthenticationNumberRepo()
                        .verificationNumberFindPassword(
                            ReqFindPasswordNumberDto(
                                email,
                                id
                            )
                        )
                        .subscribe({
                            isVerificationNumberRequestedObservable.set(true)
                            isLoadingVisibleObservable.set(false)
                            requestTimeCount()
                        }, {
                            isVerificationNumberRequestedObservable.set(false)
                            isLoadingVisibleObservable.set(false)

                            when (
                                getConvertedErrorData(
                                    it,
                                    ResFindPasswordNumberErrorDto::class.java
                                ).error.code) {
                                4002 -> {
                                    isNonExistentEmailLiveData.value = true
                                }
                            }
                        })
                )
            }
        }
    }

    private fun requestTimeCount() {
        mRemainTimeObservable.set(getConvertedTime(TIMEOUT))
        isTimeRemainsObservable.set(true)
        mCompositeDisposable.add(
            getIntervalObservable()
                .map {
                    getConvertedTime(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mRemainTimeObservable.set(it)
                }, {

                }, {
                    isTimeRemainsObservable.set(false)
                })
        )
    }

    fun requestSelfVerification(email: String, verificationNumber: Int, id: String) {
        if (isTimeRemainsObservable.get().not()) {
            isVerificationTimeoutLiveData.value = true

            return
        }

        mCompositeDisposable.add(
            Injection
                .provideAuthenticationNumberRepo()
                .verificationFindPassword(
                    ReqValidationFindPasswordNumberDto(
                        email,
                        verificationNumber,
                        id
                    )
                )
                .onErrorReturn {
                    getConvertedErrorData(it, ResAuthenticationTokenErrorDto::class.java)
                }
                .subscribe({
                    if (it is ResAuthenticationTokenErrorDto) {
                        when (it.error.code) {
                            4102 -> {
                                isVerificationNumberWrongLiveData.value = true
                            }
                        }
                    } else {
                        isVerifiedObservable.set(true)
                        mVerificationToken = it.token
                    }
                }, {
                    isVerifiedObservable.set(false)
                    mVerificationToken = null
                })
        )
    }

    fun requestResetPassword(newPassword: String, passwordConfirm: String) {
        isPasswordEmptyWarningVisibleObservable.set(newPassword.isEmpty())
        isPasswordNotIdenticalWarningVisibleObservable.set(newPassword != passwordConfirm)

        if (newPassword.isNotEmpty() && passwordConfirm.isNotEmpty()) {
            if (newPassword == passwordConfirm) {
                mCompositeDisposable.add(
                    ApiProvider
                        .provideUserChangeApi()
                        .password(
                            (if (mVerificationToken == null) ""
                            else "Bearer $mVerificationToken"),
                            ReqModifyPasswordDto(
                                newPassword
                            )
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            isPasswordInputIdenticalLiveData.value = true
                            mVerificationToken = null
                        }, {
                            it.printStackTrace()
                        })
                )
            }
        }
    }

    private fun getIntervalObservable() =
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .take(TIMEOUT + 1)

    private fun getConvertedTime(time: Long): String {
        val minute = (TIMEOUT - time) / 60
        val seconds = (TIMEOUT - time) % 60

        return if (seconds < 10) "$minute:0${seconds}" else "$minute:$seconds"
    }

    private fun <T> getConvertedErrorData(throwable: Throwable, type: Class<T>): T =
        Gson().fromJson(
            (throwable as HttpException).response().errorBody()?.string(),
            type
        )

    private fun isEmailPattern(inputEmail: String) =
        Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()

    fun clearCompositeDisposable() {
        timber.log.Timber.d("clearCompositeDisposable()")

        mCompositeDisposable.clear()
    }
}