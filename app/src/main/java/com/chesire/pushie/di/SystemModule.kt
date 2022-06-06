package com.chesire.pushie.di

import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SystemModule {

    @Provides
    fun providesClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.getSystemService<ClipboardManager>() as ClipboardManager
}
