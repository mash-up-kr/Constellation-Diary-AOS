package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
    var isEmailSend = false

    val mRemainTime = MutableLiveData<String>()
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mIntervalObservable by lazy {
        getIntervalObservable()
    }

    fun requestEmailVerification() {
        clearDisposable()

        mCompositeDisposable.add(
            mIntervalObservable
                .map {
                    getConvertedTime(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    timber.log.Timber.d("onNext()")
                    timber.log.Timber.d(it)

                    mRemainTime.postValue(it)
                }, {

                }, {

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