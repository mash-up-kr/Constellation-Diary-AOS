package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.SignRepository
import com.mashup.telltostar.data.source.remote.api.UserApi
import com.mashup.telltostar.data.source.remote.request.ReqSignUpDto
import com.mashup.telltostar.data.source.remote.response.Authentication
import com.mashup.telltostar.util.PrefUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class SignRepoImpl(
    private val api: UserApi
) : SignRepository {

    private val authorization = "Bearer ${PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")}"

    override fun sighUp(
        constellation: String,
        email: String,
        fcmToken: String,
        password: String,
        userId: String,
        token: String
    ): Single<Authentication> {
        val signUp = ReqSignUpDto(
            constellation = constellation,
            email = email,
            fcmToken = fcmToken,
            password = password,
            userId = userId
        )
        return api.signUp(token, signUp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sighOut(): Single<Response<Void>> {
        return api.signOut(authorization)
            .composeError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}