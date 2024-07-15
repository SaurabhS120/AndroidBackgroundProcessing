package com.example.androidbackgroundprocessing

import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadNotificationHelper
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadNotificationHelperInterface
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressBroadcastHelper
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressBroadcastHelperInterface
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressNotifierClient
import dagger.Binds
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
    fun provideDownloadProgressBroadcastHelper(): DownloadProgressBroadcastHelper {
        return DownloadProgressBroadcastHelper()
    }

    @Provides
    @Singleton
    fun provideDummyDownloadHelper():DummyDownloadHelper{
        return DummyDownloadHelper()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloadModuleAbstractions{

    @Binds
    @Singleton
    abstract fun provideDownloadProgressNotifierClient(downloadProgressNotifierClient:DownloadProgressNotifierClient):DownloadProgressNotifierClientInterface


    @Binds
    @Singleton
    abstract fun provideDownloadNotificationHelper(downloadNotificationHelper: DownloadNotificationHelper): DownloadNotificationHelperInterface

    @Binds
    @Singleton
    abstract fun provideDownloadProgressBroadcastHelper(downloadProgressBroadcastHelper: DownloadProgressBroadcastHelper): DownloadProgressBroadcastHelperInterface
}