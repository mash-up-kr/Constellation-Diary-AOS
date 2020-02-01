package com.mashup.telltostar.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.source.remote.ApiProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hclee on 2020-01-04.
 */

class ForgotIdViewModel {
    private val mCompositeDisposable = CompositeDisposable()
    val isEmailEmptyWarningVisibleObservable = ObservableBoolean(false)
    val isFindIdSuccess = ObservableBoolean(false)
    val mFoundIdObservable = ObservableField<String>()

    fun requestFindId(email: String) {
        isEmailEmptyWarningVisibleObservable.set(email.isEmpty())

        if (email.isNotEmpty()) {
            mCompositeDisposable.add(
                ApiProvider
                    .provideUserApi()
                    .findId(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isFindIdSuccess.set(true)
                        isEmailEmptyWarningVisibleObservable.set(false)
                        mFoundIdObservable.set(it.userId)
                    }, {
                        isFindIdSuccess.set(false)
                        isEmailEmptyWarningVisibleObservable.set(true)
                    })
            )
        }
    }

    fun clearCompositeDisposable() {
        mCompositeDisposable.clear()
    }
}