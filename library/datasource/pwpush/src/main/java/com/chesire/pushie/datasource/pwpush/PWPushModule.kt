package com.chesire.pushie.datasource.pwpush

import android.content.Context
import com.chesire.pushie.datasource.pwpush.local.PusherDB
import com.chesire.pushie.datasource.pwpush.local.dao.PushedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PWPushModule {

    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context): PusherDB = PusherDB.build(context)

    @Provides
    @Singleton
    fun providesPushedDao(db: PusherDB): PushedDao = db.pushedDao()
}
