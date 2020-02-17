package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.Authentication
import io.reactivex.Single

interface UserRepository {

    fun refreshToken(): Single<Authentication.Tokens>
}