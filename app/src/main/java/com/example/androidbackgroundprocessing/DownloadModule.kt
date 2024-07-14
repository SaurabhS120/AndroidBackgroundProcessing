package com.example.androidbackgroundprocessing

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloadModule {
    @Provides
    @Singleton
    fun provideDownloadProgressBroadcastHelper():DownloadProgressBroadcastHelper{
        return DownloadProgressBroadcastHelper()
    }
}