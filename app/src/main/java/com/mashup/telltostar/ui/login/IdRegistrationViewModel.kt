package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData

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
    val isValidId = MutableLiveData<Boolean>()

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

    fun requestCheckIdValid(id: String, password: String, confirmPassword: String) {
        isInputIdWarningTextViewVisible.postValue(id.isEmpty())
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())
        isInputConfirmPasswordWarningTextViewVisible.postValue(confirmPassword.isEmpty())

        if (id.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            isTwoPasswordIdentical.postValue(password == confirmPassword)

            if (password == confirmPassword) {

            }
        }
    }
}