package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.app.Notification
import android.content.Context
import com.example.androidbackgroundprocessing.DownloadProgressNotifierClientInterface
import com.example.androidbackgroundprocessing.DummyDownloadService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner ::class)
class DownloadProgressNotifierClientTest {
    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var downloadProgressBroadcastHelper:DownloadProgressBroadcastHelperInterface

    @Mock
    private lateinit var downloadNotificationHelper:DownloadNotificationHelperInterface
    private lateinit var client:DownloadProgressNotifierClientInterface

    @Before
    fun init(){
        client = DownloadProgressNotifierClient(
            downloadProgressBroadcastHelper,
            downloadNotificationHelper
        )
    }

    @Test
    fun buildNotification(){
        client.onCreate(mockContext)
        val notification = client.buildNotification(-1)

    }

    @Test(expected = DownloadProgressWatcherNoContext::class)
    fun buildNotificationFailTest(){
        val notification = client.buildNotification(-1)
    }

    @Test
    fun onCreate(){
        client.onCreate(mockContext)
        Mockito.verify(downloadProgressBroadcastHelper,Mockito.times(1)).onCreate(mockContext)
        Mockito.verify(downloadNotificationHelper,Mockito.times(1)).onCreate(mockContext)
    }

    @Test
    fun onUpdate(){
        client.onCreate(mockContext)
        client.onUpdate(1)
        Mockito.verify(downloadProgressBroadcastHelper,Mockito.times(1)).onUpdate(1)
        Mockito.verify(downloadNotificationHelper,Mockito.times(1)).onUpdate(1)
    }
}