package com.mashup.telltostar.ui.login.forgotid

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.response.ResUserIdErrorDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Created by hclee on 2020-01-04.
 */

class ForgotIdViewModel {
    private val mCompositeDisposable = CompositeDisposable()
    val isEmailEmptyWarningVisibleObservable = ObservableBoolean(false)
    val isNonExistentEmailWarningVisibleObservable = ObservableBoolean(false)
    val isNonExistentEmailWarningVisibleLiveData = MutableLiveData<Boolean>(false)
    val isFindIdSuccess = ObservableBoolean(false)
    val mFoundIdObservable = ObservableField<String>()

    fun requestFindId(email: String) {
        isEmailEmptyWarningVisibleObservable.set(email.isEmpty())

        if (email.isNotEmpty()) {
            mCompositeDisposable.add(
                ApiProvider
                    .provideUserApi()
                    .findId(email)
                    .onErrorReturn {
                        getConvertedErrorData(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isEmailEmptyWarningVisibleObservable.set(false)

                        if (it is ResUserIdErrorDto) {
                            when (it.error.code) {
                                4003 -> {
                                    isNonExistentEmailWarningVisibleObservable.set(true)
                                    isNonExistentEmailWarningVisibleLiveData.value = true
                                }
                            }
                        } else {
                            isNonExistentEmailWarningVisibleObservable.set(false)
                            isNonExistentEmailWarningVisibleLiveData.value = false
                            isFindIdSuccess.set(true)
                            mFoundIdObservable.set(it.userId)
                        }
                    }, {
                        isFindIdSuccess.set(false)
                    })
            )
        }
    }

    private fun getConvertedErrorData(throwable: Throwable) =
        Gson().fromJson(
            (throwable as HttpException).response().errorBody()?.string(),
            ResUserIdErrorDto::class.java
        )

    fun clearCompositeDisposable() {
        mCompositeDisposable.clear()
    }
}