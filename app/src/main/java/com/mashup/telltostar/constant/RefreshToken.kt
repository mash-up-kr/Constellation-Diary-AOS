package com.mashup.telltostar.constant

object RefreshToken {

    //refresh 무한 루프 방지
    //3회 초과 실행 시 에러
    @Volatile
    private var refreshTokenCnt = 0

    @Synchronized
    fun upCountRefreshToken() {
        refreshTokenCnt++
    }

    @Synchronized
    fun getRefreshToken(): Int {
        return refreshTokenCnt
    }

    @Synchronized
    fun initRefreshToken() {
        refreshTokenCnt = 0
    }
}

