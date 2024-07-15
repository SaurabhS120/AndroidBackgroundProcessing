package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.app.Notification
import android.content.Context
import com.example.androidbackgroundprocessing.DownloadProgressNotifierClientInterface
import com.example.androidbackgroundprocessing.DummyDownloadService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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
        val notification = client.buildNotification()

    }

    @Test(expected = DownloadProgressWatcherNoContext::class)
    fun buildNotificationFailTest(){
        val notification = client.buildNotification()
    }
}