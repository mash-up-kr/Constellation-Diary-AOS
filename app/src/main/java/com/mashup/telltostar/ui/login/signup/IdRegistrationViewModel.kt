package com.mashup.telltostar.ui.login.signup

import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.util.RegexUtil
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
    val isAvailableId = MutableLiveData<Boolean>()
    val isNotIdPatternWarningTextViewVisible = MutableLiveData<Boolean>()

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
        isConfirmPasswordVisible.value = true
    }

    fun requestConfirmPasswordInvisible() {
        isConfirmPasswordVisible.value = false
    }

    fun requestCheckIdDuplication(id: String) {
        isNotIdPatternWarningTextViewVisible.value = RegexUtil.isIdPattern(id).not()

        if (RegexUtil.isIdPattern(id)) {
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
                        isAvailableId.value = it
                    }, {
                        it.printStackTrace()
                    })
            )
        }
    }

    fun requestCheckTwoPasswordIdentical(id: String, password: String, confirmPassword: String) {
        isInputIdWarningTextViewVisible.postValue(id.isEmpty())
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())
        isInputConfirmPasswordWarningTextViewVisible.value = confirmPassword.isEmpty()

        if (id.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            isTwoPasswordIdentical.value = (password == confirmPassword)
        }
    }
}