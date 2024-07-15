package com.example.androidbackgroundprocessing

import android.app.Notification
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelperInterface
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner ::class)
class DummyDownloadServiceWorkerTest{
    @Mock
    lateinit var dummyDownloadHelper : DummyDownloadHelperInterface
    @Mock
    lateinit var dummyDownloadToastHelper:DownloadToastHelperInterface
    @Mock
    lateinit var dummyDownloadService: DummyDownloadServiceInterface
    @Mock
    lateinit var downloadProgressNotifierClient: DownloadProgressNotifierClientInterface
    @Mock
    lateinit var foregroundServiceHelper: ForegroundServiceHelperInterface

    lateinit var dummyDownloadServiceWorker: DummyDownloadServiceWorkerInterface

    val mockNotification = Mockito.mock(Notification::class.java)

    lateinit var serviceJob : Job
    lateinit var serviceScope: CoroutineScope


    @Before
    fun setup(){
        serviceJob = Job()
        serviceScope =  CoroutineScope(Dispatchers.IO + serviceJob)
        dummyDownloadServiceWorker = DummyDownloadServiceWorker(
            serviceJob = serviceJob,
            serviceScope = serviceScope,
            dummyDownloadHelper = dummyDownloadHelper,
            dummyDownloadToastHelper = dummyDownloadToastHelper,
            dummyDownloadService = dummyDownloadService,
            downloadProgressNotifierClient = downloadProgressNotifierClient,
            foregroundServiceHelper = foregroundServiceHelper
        )
    }

    @Test
    fun `Foreground Service not supported test`(){
        Mockito.`when`(downloadProgressNotifierClient.buildNotification(Mockito.anyInt())).thenAnswer {
            mockNotification
        }
        Mockito.`when`(foregroundServiceHelper.startForegroundService(mockNotification)).thenAnswer {
            false
        }
        dummyDownloadServiceWorker.onStartDownloadAction()
        Mockito.verify(dummyDownloadToastHelper,Mockito.times(1)).noNotificationNotSupported()
    }

    @Test
    fun `Start test`(){
        Mockito.`when`(downloadProgressNotifierClient.buildNotification(Mockito.anyInt())).thenAnswer {
            mockNotification
        }
        Mockito.`when`(foregroundServiceHelper.startForegroundService(mockNotification)).thenAnswer {
            true
        }
        dummyDownloadServiceWorker.onStartDownloadAction()
        runBlocking{
            serviceJob.join()
            Mockito.verify(dummyDownloadHelper, Mockito.times(1)).download()
        }
    }
}