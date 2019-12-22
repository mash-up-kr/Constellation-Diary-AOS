package com.mashup.telltostar.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mashup.telltostar.data.source.local.entity.DiaryEntity

@Database(entities = [DiaryEntity::class], version = 1)
abstract class DiaryDataBase : RoomDatabase() {

    abstract fun getDiaryDao(): DiaryDao

    companion object {

        private var INSTANCE: DiaryDataBase? = null

        fun getInstance(context: Context): DiaryDataBase {

            if (INSTANCE == null) {
                synchronized(DiaryDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDataBase::class.java,
                        "diary.db"
                    )
                        //테스트용 : 빌드 시 마다 기존 데이터베이스 삭제
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE!!
        }

    }

}