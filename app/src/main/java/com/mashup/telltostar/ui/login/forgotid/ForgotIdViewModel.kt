package com.mashup.telltostar.ui.login.forgotid

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.data.source.remote.response.ResUserIdErrorDto
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hclee on 2020-01-04.
 */

@Singleton
class ForgotIdViewModel @Inject constructor() {
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
                Injection
                    .provideUserRepo()
                    .findId(email)
                    .onErrorReturn {
                        getConvertedErrorData(it)
                    }
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