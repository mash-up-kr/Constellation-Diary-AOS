package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hclee on 2020-01-04.
 */

class ForgotIdViewModel {
    private val mCompositeDisposable = CompositeDisposable()
    val isEmailVerified = MutableLiveData<Boolean>()

    fun requestEmailVerification(verificationNumber: String) {
        // TODO: 메일 인증 서버에 요청
        mCompositeDisposable.add(
            Single.just(Random().nextInt(2))
                .subscribeOn(Schedulers.io())
                .delay(1500, TimeUnit.MILLISECONDS)
                .map {
                    it > 0
                }
                .subscribe({
                    isEmailVerified.postValue(it)

                    timber.log.Timber.d("$it")
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun clearCompositeDisposable() {
        mCompositeDisposable.clear()
    }
}