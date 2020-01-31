package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqSignInDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by hclee on 2020-01-12.
 */

class LoginViewModel {
    val isInputIdWarningTextViewVisible = MutableLiveData<Boolean>()
    val isInputPasswordWarningTextViewVisible = MutableLiveData<Boolean>()
    val isLoggedIn = MutableLiveData<Boolean>(false)
    private val mCompositeData by lazy {
        CompositeDisposable()
    }

    fun requestLogin(timeZone: String, id: String, password: String) {
        isInputIdWarningTextViewVisible.postValue(id.isEmpty())
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())

        if (id.isNotEmpty() && password.isNotEmpty()) {
            mCompositeData.add(
                ApiProvider
                    .provideUserApi()
                    .signIn(
                        timeZone,
                        ReqSignInDto(
                            "temptemp",
                            id,
                            password
                        )
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isLoggedIn.postValue(true)
                    }, {
                        isLoggedIn.postValue(false)
                    })
            )
        }
    }
}