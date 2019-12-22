package com.mashup.telltostar.data

import android.content.Context
import com.mashup.telltostar.data.repository.DiaryRepository
import com.mashup.telltostar.data.source.local.DiaryDataBase

object Injection {

    fun provideDiaryRepository(context: Context): DiaryRepository {
        val database = DiaryDataBase.getInstance(context)
        return DiaryRepository(
            database.getDiaryDao()
            //ApiProvider.provideDiaryApi()
        )
    }
}