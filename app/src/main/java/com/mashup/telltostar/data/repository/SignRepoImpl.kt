package com.mashup.telltostar.data.repository

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

    override fun sighUp(
        constellation: String,
        email: String,
        password: String,
        userId: String
    ): Single<Authentication> {
        val authorization = PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")
        val signUp = ReqSignUpDto(
            constellation = constellation,
            email = email,
            password = password,
            userId = userId
        )
        return api.signUp(authorization, signUp)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sighOut(): Single<Response<Void>> {
        val authorization = PrefUtil.get(PrefUtil.AUTHENTICATION_TOKEN, "")
        return api.signOut(authorization)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}