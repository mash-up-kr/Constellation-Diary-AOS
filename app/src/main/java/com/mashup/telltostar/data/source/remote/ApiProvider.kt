package com.mashup.telltostar.data.source.remote

import com.mashup.telltostar.data.source.remote.api.DailyApi
import com.mashup.telltostar.data.source.remote.api.DiaryApi
import com.mashup.telltostar.data.source.remote.api.HoroscopeApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    private const val BASE_URL = "https://byeol-byeol.kro.kr/"

    fun provideAuthenticationNumberApi() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthenticationNumberApi::class.java)

    fun provideUserApi() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApi::class.java)

    fun provideDiaryApi(): DiaryApi = retrofit.build()
        .create(DiaryApi::class.java)

    fun provideHoroscopeApi(): HoroscopeApi = retrofit.build()
        .create(HoroscopeApi::class.java)

    fun provideDailyApi(): DailyApi = retrofit.build()
        .create(DailyApi::class.java)

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())

    // 네트뭐크 통신에 사용할 클라이언트 객체를 생성합니다.
    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val b = OkHttpClient.Builder()
        // 이 클라이언트를 통해 오고 가는 네트워크 요청/응답을 로그로 표시하도록 합니다.
        b.addInterceptor(interceptor)
        // header 에 정보를 추가해 준다.
        b.addInterceptor(AddHeaderInterceptor())
        return b.build()
    }

    // 네트워크 요청/응답을 로그에 표시하는 Interceptor 객체를 생성합니다.
    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}