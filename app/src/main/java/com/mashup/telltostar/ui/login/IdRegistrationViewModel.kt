package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.source.remote.ApiProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by hclee on 2020-01-10.
 */

object IdRegistrationViewModel {
    val isPasswordVisible = MutableLiveData<Boolean>(false)
    val isConfirmPasswordVisible = MutableLiveData<Boolean>(false)
    val isTwoPasswordIdentical = MutableLiveData<Boolean>()
    val isInputIdWarningTextViewVisible = MutableLiveData<Boolean>()
    val isInputPasswordWarningTextViewVisible = MutableLiveData<Boolean>()
    val isInputConfirmPasswordWarningTextViewVisible = MutableLiveData<Boolean>()
    val isValidId = MutableLiveData<Boolean>(false)
    private val mCompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun requestPasswordVisible() {
        isPasswordVisible.postValue(true)
    }

    fun requestPasswordInvisible() {
        isPasswordVisible.postValue(false)
    }

    fun requestConfirmPasswordVisible() {
        isConfirmPasswordVisible.postValue(true)
    }

    fun requestConfirmPasswordInvisible() {
        isConfirmPasswordVisible.postValue(false)
    }

    fun requestCheckIdDuplication(id: String) {
        if (id.isNotEmpty()) {
            mCompositeDisposable.add(
                ApiProvider
                    .provideUserApi()
                    .check(id)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        Single.just(it.available)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                    }, {
                        it.printStackTrace()
                    })
            )
        }
    }

    fun requestCheckIdValid(id: String, password: String, confirmPassword: String) {
        isInputIdWarningTextViewVisible.postValue(id.isEmpty())
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())
        isInputConfirmPasswordWarningTextViewVisible.postValue(confirmPassword.isEmpty())

        if (id.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            isTwoPasswordIdentical.postValue(password == confirmPassword)

            if (password == confirmPassword) {
                mCompositeDisposable.add(
                    ApiProvider
                        .provideUserApi()
                        .check(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            isValidId.postValue(true)
                        }, {
                            isValidId.postValue(false)
                        })
                )
            }
        }
    }
}