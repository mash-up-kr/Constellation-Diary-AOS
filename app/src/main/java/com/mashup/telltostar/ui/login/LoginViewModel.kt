package com.mashup.telltostar.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mashup.telltostar.data.source.remote.ApiProvider
import com.mashup.telltostar.data.source.remote.request.ReqSignInDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoDto
import com.mashup.telltostar.data.source.remote.response.ResUserInfoErrorDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

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

    fun requestLogin(timeZone: String, fcmToken: String, id: String, password: String) {
        isInputIdWarningTextViewVisible.postValue(id.isEmpty())
        isInputPasswordWarningTextViewVisible.postValue(password.isEmpty())

        if (id.isNotEmpty() && password.isNotEmpty()) {
            mCompositeData.add(
                ApiProvider
                    .provideUserApi()
                    .signIn(
                        timeZone,
                        ReqSignInDto(
                            fcmToken,
                            id,
                            password
                        )
                    )
                    .onErrorReturn {
                        getConvertedErrorData(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it is ResUserInfoDto) {
                            isLoggedIn.postValue(true)
                        } else if (it is ResUserInfoErrorDto) {
                            when (it.error.code) {
                                4104, 4105 -> {
                                    timber.log.Timber.d("가입되지 않은 아이디이거나, 잘못된 비밀번호입니다.")
                                }
                            }
                        }
                    }, {
                        isLoggedIn.postValue(false)
                    })
            )
        }
    }

    private fun getConvertedErrorData(throwable: Throwable) =
        Gson().fromJson(
            (throwable as HttpException).response().errorBody()?.string(),
            ResUserInfoErrorDto::class.java
        )
}