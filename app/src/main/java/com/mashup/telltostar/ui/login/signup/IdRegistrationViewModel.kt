package com.mashup.telltostar.ui.login.signup

import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.Injection
import com.mashup.telltostar.util.RegexUtil
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hclee on 2020-01-10.
 */

@Singleton
class IdRegistrationViewModel @Inject constructor() {
    val isPasswordVisible = MutableLiveData<Boolean>(false)
    val isConfirmPasswordVisible = MutableLiveData<Boolean>(false)
    val isTwoPasswordIdentical = MutableLiveData<Boolean>()
    val isInputIdWarningTextViewVisible = MutableLiveData<Boolean>()
    val isInputPasswordWarningTextViewVisible = MutableLiveData<Boolean>()
    val isInputConfirmPasswordWarningTextViewVisible = MutableLiveData<Boolean>()
    val isAvailableId = MutableLiveData<Boolean>()
    val isNotIdPatternWarningTextViewVisible = MutableLiveData<Boolean>()
    val isNotPasswordPatternWarningTextViewVisible = MutableLiveData<Boolean>()
    val shouldIdDuplicationCheck = MutableLiveData<Boolean>()
    private var mFcmToken: String? = null
    private var mLastDuplicationCheckedId = ""

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
                Injection
                    .provideSignRepo()
                    .check(id)
                    .flatMap {
                        Single.just(it.available)
                    }
                    .subscribe({
                        isAvailableId.value = it
                        mLastDuplicationCheckedId = id
                    }, {
                        it.printStackTrace()
                    })
            )
        }
    }

    fun requestCheckTwoPasswordIdentical(id: String, password: String, confirmPassword: String) {
        if (id.isEmpty()) {
            isInputIdWarningTextViewVisible.postValue(true)
        }

        isNotPasswordPatternWarningTextViewVisible.value = false
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())
        isInputConfirmPasswordWarningTextViewVisible.value = confirmPassword.isEmpty()

        if (id.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (isDuplicationCheckedId(id)) {
                shouldIdDuplicationCheck.value = false

                if (RegexUtil.isPasswordPattern(password)) {
                    isTwoPasswordIdentical.value = (password == confirmPassword)
                } else {
                    isNotPasswordPatternWarningTextViewVisible.value = true
                }
            } else {
                shouldIdDuplicationCheck.value = true
            }
        }
    }

    private fun isDuplicationCheckedId(id: String) =
        isAvailableId.value?.let {
            (it && mLastDuplicationCheckedId == id)
        } ?: false

    fun getFcmToken() = mFcmToken

    fun setFcmToken(token: String?) {
        mFcmToken = token
    }
}