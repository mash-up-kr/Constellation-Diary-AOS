package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData

/**
 * Created by hclee on 2020-01-17.
 */

object ForgotPasswordViewModel {
    val isIdCheckWarningTextViewVisible: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)
    val isEmailCheckWarningTextViewVisible: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)
    val isVerificationNumberInputVisible: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)

    fun requestVerificationNumber(id: String, email: String) {
        if (id.isNotEmpty() && email.isNotEmpty()) {
            // TODO: 비밀번호 찾기용 인증번호 요청 API
        }
    }
}