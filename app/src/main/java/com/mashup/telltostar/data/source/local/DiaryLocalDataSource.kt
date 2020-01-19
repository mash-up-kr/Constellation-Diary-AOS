package com.mashup.telltostar.data.source.local

import com.mashup.telltostar.data.Diary
import com.mashup.telltostar.data.mapToEntity
import com.mashup.telltostar.data.source.DiaryDataSource
import com.mashup.telltostar.data.source.local.entity.mapToPresentation
import com.mashup.telltostar.util.TimeUtil
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DiaryLocalDataSource(
    private val diaryDao: DiaryDao
) : DiaryDataSource {

    override fun getDiaries(): Single<List<Diary>> {
        return diaryDao.getDiaries().map { it.map { it.mapToPresentation() } }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTodayDiaries(): Single<List<Diary>> {
        return diaryDao.getDiaries().flatMap {

            val currentTime = TimeUtil.getDateInteger(TimeUtil.getCurrentUnixTime())
            val diaries = it.filter { diary ->

                val diaryTime = TimeUtil.getDateInteger(diary.date.toLong())
                val temp = currentTime - diaryTime

                Timber.e("currentTime : $currentTime , diaryTime : $diaryTime -> temp : $temp")
                temp == 0
            }

            Single.just(diaries.map { it.mapToPresentation() })
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getDiaryById(id: Int): Single<Diary> {
        return diaryDao.getDiaryById(id).map { it.mapToPresentation() }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(id: Int): Completable {
        return Completable.fromCallable { diaryDao.delete(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(diary: Diary): Completable {
        return Completable.fromCallable {
            diaryDao.getDiaryById(diary.id)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insert(diary: Diary): Completable {
        return Completable.fromCallable { diaryDao.insert(diary.mapToEntity()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(diary: Diary): Completable {
        return Completable.fromCallable { diaryDao.update(diary.mapToEntity()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun clearAll(): Completable {
        return Completable.fromCallable { diaryDao.clearAll() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}