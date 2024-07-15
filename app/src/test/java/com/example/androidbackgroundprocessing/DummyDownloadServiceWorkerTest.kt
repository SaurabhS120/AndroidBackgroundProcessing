package com.example.androidbackgroundprocessing

import android.app.Notification
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelperInterface
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner ::class)
class DummyDownloadServiceWorkerTest{
    val dummyDownloadHelper = object : DummyDownloadHelper() {
        override suspend fun dummyDelay() {
            //No delay
        }
    }
    @Mock
    lateinit var dummyDownloadToastHelper:DownloadToastHelperInterface
    @Mock
    lateinit var dummyDownloadService:DummyDownloadServiceInterface
    @Mock
    lateinit var downloadProgressNotifierClient:DownloadProgressNotifierClientInterface

    lateinit var dummyDownloadServiceWorker:DummyDownloadServiceWorkerInterface

    @Before
    fun setup(){
        dummyDownloadServiceWorker = DummyDownloadServiceWorker(
            dummyDownloadHelper = dummyDownloadHelper,
            dummyDownloadToastHelper = dummyDownloadToastHelper,
            dummyDownloadService = dummyDownloadService,
            downloadProgressNotifierClient = downloadProgressNotifierClient
        )
    }

    @Test
    fun `Foreground Service not supported test`(){
        Mockito.`when`(downloadProgressNotifierClient.buildNotification(-1)).thenAnswer {
            Mockito.mock(Notification::class.java)
        }
        dummyDownloadServiceWorker.onStartDownloadAction()
        Mockito.verify(dummyDownloadToastHelper,Mockito.times(1)).noNotificationNotSupported()
    }
}