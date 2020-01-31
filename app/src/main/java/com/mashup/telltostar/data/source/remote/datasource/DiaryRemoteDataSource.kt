package com.mashup.telltostar.data.source.remote.datasource

import com.mashup.telltostar.data.source.DiaryDataRepository
import com.mashup.telltostar.data.source.remote.api.DiaryApi
import com.mashup.telltostar.data.source.remote.request.DiaryDto
import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Single
import retrofit2.Response

class DiaryRemoteDataSource(
    private val diaryApi: DiaryApi
) : DiaryDataRepository {

    override fun get(id: Int): Single<Diary> {
        return diaryApi.getDiary(id)
    }

    override fun gets(month: Int, year: Int): Single<Diaries> {
        return diaryApi.getDiaries(month, year)
    }

    override fun insert(
        horoscopeId: Int,
        title: String,
        content: String
    ): Single<Response<Void>> {

        val date = TimeUtil.getUTCDate()

        val dairyDto = DiaryDto(
            horoscopeId = horoscopeId,
            title = title,
            content = content,
            date = date
        )

        return diaryApi.postDiaries(dairyDto)
    }

    override fun update(
        id: Int,
        horoscopeId: Int,
        title: String,
        content: String
    ): Single<Any> {

        val date = TimeUtil.getUTCDate()

        val dairyDto = DiaryDto(
            horoscopeId = horoscopeId,
            title = title,
            content = content,
            date = date
        )

        return diaryApi.putDiary(id, dairyDto)
    }

    override fun delete(id: Int): Single<Response<Void>> {
        return diaryApi.deleteDiary(id)
    }
}