package com.mashup.telltostar.data.repository

import com.mashup.telltostar.data.source.UserRepository
import com.mashup.telltostar.data.source.remote.api.UserApi
import com.mashup.telltostar.data.source.remote.response.Authentication
import com.mashup.telltostar.util.PrefUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class UserRepoImpl(
    private val userApi: UserApi
) : UserRepository {
    private val refreshToken = PrefUtil.get(PrefUtil.REFRESH_TOKEN, "")

    override fun refreshToken(): Single<Authentication.Tokens> {
        return userApi.tokens(refreshToken)
            .flatMap {
                Timber.d("refreshToken : $it")
                PrefUtil.put(PrefUtil.AUTHENTICATION_TOKEN, it.authenticationToken)
                PrefUtil.put(PrefUtil.REFRESH_TOKEN, it.refreshToken)

                Single.just(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}