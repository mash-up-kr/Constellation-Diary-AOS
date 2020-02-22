package com.mashup.telltostar.data.exception

import com.mashup.telltostar.constant.RefreshToken
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

internal class NetworkExceptionSingleTransformer<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.onErrorResumeNext {
            Single.error(
                if (it is HttpException) {
                    if (it.code() == 401) {
                        it.response().errorBody()?.let { errorBody ->
                            val jsonObject = JSONObject(errorBody.string())
                            val error = jsonObject.getJSONObject("error")
                            val code = error.get("code")

                            Timber.d("error : $error")

                            if (code == 4101) {
                                //Fail Authentication check token
                                RefreshToken.upCountRefreshToken()
                                Timber.d("error : $error -> ${RefreshToken.getRefreshToken()}")

                                if (RefreshToken.getRefreshToken() > 4) {
                                    RefreshToken.initRefreshToken()

                                    Exception.OverRefreshException(
                                        "refresh token over three times",
                                        it.code()
                                    )
                                } else {
                                    Exception.AuthenticationException(it.message(), it.code())
                                }
                            } else {
                                it
                            }
                        }
                    } else {
                        it
                    }
                } else it
            )
        }
}

internal fun <T> Single<T>.composeError() =
    compose(NetworkExceptionSingleTransformer())
