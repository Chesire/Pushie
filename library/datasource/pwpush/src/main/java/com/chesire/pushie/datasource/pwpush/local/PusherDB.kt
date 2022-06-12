package com.chesire.pushie.datasource.pwpush.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chesire.pushie.datasource.pwpush.local.dao.PushedDao
import com.chesire.pushie.datasource.pwpush.local.entity.PushedEntity

private const val DATABASE_NAME = "pusher_db.db"

@Database(
    entities = [PushedEntity::class],
    version = 1
)
abstract class PusherDB : RoomDatabase() {

    abstract fun pushedDao(): PushedDao

    companion object {
        fun build(context: Context): PusherDB {
            return Room
                .databaseBuilder(context, PusherDB::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
