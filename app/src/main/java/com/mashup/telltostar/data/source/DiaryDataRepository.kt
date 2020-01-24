package com.mashup.telltostar.data.source

import com.mashup.telltostar.data.source.remote.response.Diaries
import com.mashup.telltostar.data.source.remote.response.Diary
import io.reactivex.Single

interface DiaryDataRepository {

    fun get(id: Int): Single<Diary>

    fun gets(month: Int, year: Int): Single<Diaries>

    fun insert(horoscopeId: Int, title: String, content: String): Single<Any>

    fun update(id: Int, horoscopeId: Int, title: String, content: String): Single<Any>

    fun delete(id: Int): Single<Any>

}