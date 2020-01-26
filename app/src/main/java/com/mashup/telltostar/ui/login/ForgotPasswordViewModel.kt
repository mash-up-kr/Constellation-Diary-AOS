package com.mashup.telltostar.ui.login

import android.util.Patterns
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.ReqVerificationFindPassword
import com.mashup.telltostar.data.source.remote.ReqVerificationNumberFindPassword
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by hclee on 2020-01-17.
 */

object ForgotPasswordViewModel {
    private const val TIMEOUT = 180L
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

    fun requestVerificationNumber(id: String, email: String) {
        isIdEmptyWarningVisibleObservable.set(id.isEmpty())
        isEmailEmptyWarningVisibleObservable.set(email.isEmpty() || isEmailPattern(email).not())

        if (id.isNotEmpty() && email.isNotEmpty()) {
            if (isEmailPattern(email)) {
                mCompositeDisposable.clear()
                isLoadingVisibleObservable.set(true)
                mCompositeDisposable.add(
                    ApiProvider
                        .provideAuthenticationNumberApi()
                        .verificationNumberFindPassword(
                            ReqVerificationNumberFindPassword(
                                email,
                                id
                            )
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            isVerificationNumberRequestedObservable.set(true)
                            isLoadingVisibleObservable.set(false)
                            requestTimeCount()
                        }, {
                            isVerificationNumberRequestedObservable.set(false)
                            isLoadingVisibleObservable.set(false)
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

    fun requestResetPassword(email: String, verificationNumber: Int, id: String) {
        mCompositeDisposable.add(
            ApiProvider
                .provideAuthenticationNumberApi()
                .verificationFindPassword(
                    ReqVerificationFindPassword(
                        email,
                        verificationNumber,
                        id
                    )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isVerifiedObservable.set(true)
                }, {
                    isVerifiedObservable.set(false)
                })
        )
    }

    private fun getIntervalObservable() =
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .take(TIMEOUT + 1)

    private fun getConvertedTime(time: Long): String {
        val minute = (TIMEOUT - time) / 60
        val seconds = (TIMEOUT - time) % 60

        return if (seconds < 10) "$minute:0${seconds}" else "$minute:$seconds"
    }

    private fun isEmailPattern(inputEmail: String) =
        Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()
}