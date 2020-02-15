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
 * 이 ViewModel의 객체는 SignUpFragment, EmailVerificationFragment, IdRegistrationFragment
 * 3개의 fragment에서 동일한 하나의 객체를 참조해야 한다. 싱글턴으로 만들지 않는 이유는 회원가입 프로세스 진행 중에
 * 사용자가 뒤로가기를 선택하거나 BottomSheet을 닫아 버리는 경우에 회원가입 프로세스가 처음부터 진행되어야 하는데,
 * 이 ViewModel이 갖고있는 상태 멤버 field들이 초기화 되어야 하기 때문이다.
 */
class EmailVerificationViewModel {
    companion object {
        private const val TIMEOUT = 180L
    }

    val isEmailPattern = MutableLiveData<Boolean>()
    val isEmailSend = MutableLiveData<Boolean>(false)
    val isEmailVerified = MutableLiveData<Boolean>()
    val mInputVerificationNumber = MutableLiveData<String>()
    val isVerificationTimeoutLiveData = MutableLiveData<Boolean>(false)
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mIntervalObservable by lazy {
        getIntervalObservable()
    }
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
                                isVerificationTimeout = true
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
        if (isVerificationTimeout) {
            isVerificationTimeoutWarningVisibleObservable.set(true)
            isVerificationTimeoutLiveData.value = true
        } else {
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