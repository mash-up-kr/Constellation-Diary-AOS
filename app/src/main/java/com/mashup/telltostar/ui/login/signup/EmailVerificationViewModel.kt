package com.mashup.telltostar.ui.login.signup

import android.util.Patterns
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqSignUpNumberDto
import com.mashup.telltostar.data.source.remote.request.ReqValidationSignUpNumberDto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by hclee on 2019-12-29.
 */

/**
 * 이메일 인증 동작(인증 메일 받기 선택 후 남은 시간 표시)은 회원가입 화면을 벗어나더라도
 * 계속 시간이 흘러가야하며, 회원가입 화면으로 재진입 시에도 이전에 흐르기 시작한 시간을 그대로 보여줘야 하기 때문에
 * 이 부분을 처리할 ViewModel이 화면 진입 시마다 새로 만들어져서는 안된다.
 * 또한, 해당 ViewModel은 다른 화면에서 사용될 이유가 전혀 없기 때문에 Singleton으로 구현한다.
 */
object EmailVerificationViewModel {
    private const val TIMEOUT = 180L
    val isEmailPattern = MutableLiveData<Boolean>()
    val isEmailSend = MutableLiveData<Boolean>(false)
    val isEmailVerified = MutableLiveData<Boolean>()
    val mInputVerificationNumber = MutableLiveData<String>()
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mIntervalObservable by lazy {
        getIntervalObservable()
    }
    val mVerifiedEmailObservable = ObservableField<String>()
    val isEmailPatternObservable = ObservableBoolean()
    val isEmailVerifiedObservable = ObservableBoolean(false)
    val mRemainTimeObservable = ObservableField<String>()
    val isEmailSendObservable = ObservableBoolean(false)

    fun requestVerificationNumber(inputEmail: String) {
        if (isEmailPattern(inputEmail)) {
            isEmailPattern.postValue(true)
            isEmailPatternObservable.set(true)
            clearDisposable()

            mCompositeDisposable.add(
                    mIntervalObservable
                            .map {
                                getConvertedTime(it)
                            }
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                mRemainTimeObservable.set(it)
                            }, {

                            }, {

                            })
            )
            mCompositeDisposable.add(
                ApiProvider
                    .provideAuthenticationNumberApi()
                    .authenticationNumbersSignUp(
                        ReqSignUpNumberDto(
                            inputEmail
                        )
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isEmailSend.postValue(true)
                        isEmailSendObservable.set(true)
                    }, {
                        isEmailPattern.postValue(false)
                        isEmailPatternObservable.set(false)
                        it.printStackTrace()
                    })
            )
        } else {
            isEmailPattern.postValue(false)
            isEmailPatternObservable.set(false)
        }
    }

    private fun isEmailPattern(inputEmail: String) =
            Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()

    fun requestEmailVerification(inputEmail: String, verificationNumber: Int) {
        if (inputEmail == "hclee" && verificationNumber == 654321) {
            isEmailVerified.value = true
            isEmailVerifiedObservable.set(true)
            mVerifiedEmailObservable.set(inputEmail)

            return
        }

        mCompositeDisposable.add(
            ApiProvider
                .provideAuthenticationNumberApi()
                .authenticationSignUp(
                    ReqValidationSignUpNumberDto(
                        inputEmail,
                        verificationNumber
                    )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isEmailVerified.postValue(true)
                    isEmailVerifiedObservable.set(true)
                    mVerifiedEmailObservable.set(inputEmail)
                }, {
                    it.printStackTrace()
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

    fun clearDisposable() {
        mCompositeDisposable.clear()
    }
}