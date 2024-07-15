package com.example.androidbackgroundprocessing

import android.app.Notification
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelperInterface
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
    lateinit var mockDummyDownloadHelper : DummyDownloadHelperInterface
    @Mock
    lateinit var mockDummyDownloadToastHelper:DownloadToastHelperInterface
    @Mock
    lateinit var mockDummyDownloadService: DummyDownloadServiceInterface
    @Mock
    lateinit var mockDownloadProgressNotifierClient: DownloadProgressNotifierClientInterface
    @Mock
    lateinit var mockForegroundServiceHelper: ForegroundServiceHelperInterface

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
            dummyDownloadHelper = mockDummyDownloadHelper,
            dummyDownloadToastHelper = mockDummyDownloadToastHelper,
            dummyDownloadService = mockDummyDownloadService,
            downloadProgressNotifierClient = mockDownloadProgressNotifierClient,
            foregroundServiceHelper = mockForegroundServiceHelper
        )
    }

    @Test
    fun `Foreground Service not supported test`(){
        Mockito.`when`(mockDownloadProgressNotifierClient.buildNotification(Mockito.anyInt())).thenAnswer {
            mockNotification
        }
        Mockito.`when`(mockForegroundServiceHelper.startForegroundService(mockNotification)).thenAnswer {
            false
        }
        dummyDownloadServiceWorker.onStartDownloadAction()
        Mockito.verify(mockDummyDownloadToastHelper,Mockito.times(1)).noNotificationNotSupported()
    }

    @Test
    fun `Start test`(){
        Mockito.`when`(mockDownloadProgressNotifierClient.buildNotification(Mockito.anyInt())).thenAnswer {
            mockNotification
        }
        Mockito.`when`(mockForegroundServiceHelper.startForegroundService(mockNotification)).thenAnswer {
            true
        }
        dummyDownloadServiceWorker.onStartDownloadAction()
        runBlocking{
            serviceJob.join()
            Mockito.verify(mockDummyDownloadHelper, Mockito.times(1)).download()
        }
    }
}