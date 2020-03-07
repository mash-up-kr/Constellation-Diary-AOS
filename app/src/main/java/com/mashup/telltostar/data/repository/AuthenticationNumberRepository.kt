package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.exception.composeError
import com.mashup.telltostar.data.source.remote.api.AuthenticationNumberApi
import com.mashup.telltostar.data.source.remote.request.ReqSignUpNumberDto
import com.mashup.telltostar.data.source.remote.request.ReqValidationSignUpNumberDto
import com.mashup.telltostar.data.source.remote.response.ResAuthenticationTokenDto
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by hclee on 2020-03-07.
 */

class AuthenticationNumberRepository(
    private val mAuthenticationNumberApi: AuthenticationNumberApi
) {

    fun authenticationNumbersSignUp(body: ReqSignUpNumberDto): Completable {
        return mAuthenticationNumberApi
            .authenticationNumbersSignUp(body)
            .subscribeOn(Schedulers.io())
            .composeError()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun authenticationSignUp(body: ReqValidationSignUpNumberDto): Single<ResAuthenticationTokenDto> {
        return mAuthenticationNumberApi
            .authenticationSignUp(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}