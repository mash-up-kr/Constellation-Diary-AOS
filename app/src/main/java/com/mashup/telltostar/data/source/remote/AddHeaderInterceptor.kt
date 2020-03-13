package com.mashup.telltostar.data.source.remote

import okhttp3.Interceptor
import okhttp3.Response

// https://github.com/square/okhttp/wiki/Interceptors
class AddHeaderInterceptor : Interceptor {

    private val testToken =
        "Bearer eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMzODQifQ.eyJpc3MiOiJBUEkgU2VydmVyIiwic3ViIjoiQXV0aGVudGljYXRpb24gVG9rZW4iLCJleHAiOjE5MjQ5MzI3MDksImlhdCI6MTU3OTMzMjcwOSwidXNlcklkIjoidGVzdCIsImlkIjoyfQ.ZfMQWUjZ4uhubsO_XIG-8uxOYizTofeV_RF2AY6cfmHc-g5qUmEHPpUqBS8NXQEJ"

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                //.addHeader("Authorization", testToken)
                .addHeader("Time-Zone", "KST")
                .build()
        )
    }
}