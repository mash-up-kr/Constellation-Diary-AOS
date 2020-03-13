package com.mashup.telltostar.data.exception

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import retrofit2.HttpException

/**
 * Created by hclee on 2020-02-23.
 */

internal class NetworkExceptionCompletableTransformer : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource =
        upstream.onErrorResumeNext {
            Completable.error(
                if (it is HttpException) {
                    it.response().errorBody()?.let { errorBody ->
                        if (it.code() == 400) {
                            val errorString = errorBody.string()
                            val code = errorString.substringAfter("code\":").substring(0, 4)

                            if (code == "4009") {
                                Exception.ExistEmailException(it.message(), it.code())
                            } else {
                                it
                            }
                        } else {
                            it
                        }
                    }
                } else {
                    it
                }
            )
        }
}

internal fun Completable.composeError() =
    compose(NetworkExceptionCompletableTransformer())