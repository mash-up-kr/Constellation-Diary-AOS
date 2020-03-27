package com.mashup.telltostar.ui.login.signup

import android.util.Patterns
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.exception.Exception
import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqSignUpNumberDto
import com.mashup.telltostar.data.source.remote.request.ReqValidationSignUpNumberDto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hclee on 2019-12-29.
 */

/**
 * 이 ViewModel의 객체는 SignUpFragment, EmailVerificationFragment, IdRegistrationFragment
 * 3개의 fragment에서 동일한 하나의 객체를 참조해야 한다. 싱글턴으로 만들지 않는 이유는 회원가입 프로세스 진행 중에
 * 사용자가 뒤로가기를 선택하거나 BottomSheet을 닫아 버리는 경우에 회원가입 프로세스가 처음부터 진행되어야 하는데,
 * 이 ViewModel이 갖고있는 상태 멤버 field들이 초기화 되어야 하기 때문이다.
 */

@Singleton
class EmailVerificationViewModel @Inject constructor() {
    companion object {
        private const val TIMEOUT = 10 * 60L
    }

    val isEmailPattern = MutableLiveData<Boolean>()
    val isEmailSend = MutableLiveData<Boolean>(false)
    val isEmailVerified = MutableLiveData<Boolean>()
    val mInputVerificationNumber = MutableLiveData<String>()
    val isVerificationTimeoutLiveData = MutableLiveData<Boolean>(false)
    val isExistEmailLiveData = MutableLiveData<Boolean>()
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mIntervalObservable by lazy {
        getIntervalObservable()
    }
    val shouldShowLoadingInteractionObservable = ObservableBoolean(false)
    val mVerifiedEmailObservable = ObservableField<String>()
    val isEmailPatternObservable = ObservableBoolean(true)
    val isEmailVerifiedObservable = ObservableBoolean(false)
    val mRemainTimeObservable = ObservableField<String>()
    val isEmailSendObservable = ObservableBoolean(false)
    val isVerificationTimeoutWarningVisibleObservable = ObservableBoolean(false)
    var mToken: String? = null
    private var isVerificationTimeout = false

    fun requestVerificationNumber(inputEmail: String) {
        isVerificationTimeout = false
        isVerificationTimeoutWarningVisibleObservable.set(false)
        isEmailSend.postValue(false)
        isEmailSendObservable.set(false)
        isExistEmailLiveData.value = false
        shouldShowLoadingInteractionObservable.set(false)

        if (isEmailPattern(inputEmail)) {
            isEmailPattern.postValue(true)
            isEmailPatternObservable.set(true)
            clearDisposable()

            mCompositeDisposable.add(
                Injection
                    .provideAuthenticationNumberRepo()
                    .authenticationNumbersSignUp(
                        ReqSignUpNumberDto(
                            inputEmail
                        )
                    )
                    .subscribe({
                        isEmailSend.postValue(true)
                        isEmailSendObservable.set(true)
                        isExistEmailLiveData.value = false
                        shouldShowLoadingInteractionObservable.set(true)

                        startRemainTimeCheck()
                    }, {
                        it.printStackTrace()

                        if (it is Exception.ExistEmailException) {
                            isExistEmailLiveData.value = true
                        }
                    })
            )
        } else {
            isEmailPattern.postValue(false)
            isEmailPatternObservable.set(false)
        }
    }

    private fun startRemainTimeCheck() {
        mCompositeDisposable.add(
            mIntervalObservable
                .map {
                    getConvertedTime(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    shouldShowLoadingInteractionObservable.set(false)
                    mRemainTimeObservable.set(it)
                }, {

                }, {
                    isVerificationTimeout = true
                })
        )
    }

    private fun isEmailPattern(inputEmail: String) =
            Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()

    fun requestEmailVerification(inputEmail: String, verificationNumber: Int) {
        if (isVerificationTimeout) {
            isVerificationTimeoutWarningVisibleObservable.set(true)
            isVerificationTimeoutLiveData.value = true
        } else {
            mCompositeDisposable.add(
                Injection
                    .provideAuthenticationNumberRepo()
                    .authenticationSignUp(
                        ReqValidationSignUpNumberDto(
                            inputEmail,
                            verificationNumber
                        )
                    )
                    .subscribe({
                        isEmailVerified.postValue(true)
                        isEmailVerifiedObservable.set(true)
                        mVerifiedEmailObservable.set(inputEmail)
                        mToken = it.token
                    }, {
                        it.printStackTrace()
                        isEmailVerified.value = false
                        isEmailVerifiedObservable.set(false)
                    })
            )
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

    fun clearDisposable() {
        mCompositeDisposable.clear()
    }
}