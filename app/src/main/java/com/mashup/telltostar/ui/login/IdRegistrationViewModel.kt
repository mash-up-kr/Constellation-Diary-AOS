package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by hclee on 2020-01-10.
 */

object IdRegistrationViewModel {
    private val mInputPassword = StringBuilder()
    val isPasswordVisible = MutableLiveData<Boolean>(false)
    val isConfirmPasswordVisible = MutableLiveData<Boolean>(false)

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
}