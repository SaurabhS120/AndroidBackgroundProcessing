package com.example.androidbackgroundprocessing

import android.app.Notification
import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.pm.ServiceInfo
import android.os.Build
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressNotifierClient
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelperInterface
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DummyDownloadServiceWorker(
    val dummyDownloadHelper : DummyDownloadHelperInterface,
    val downloadProgressNotifierClient: DownloadProgressNotifierClientInterface,
    val dummyDownloadService: DummyDownloadServiceInterface,
    val dummyDownloadToastHelper: DownloadToastHelperInterface,
    val foregroundServiceHelper: ForegroundServiceHelperInterface,
    private val serviceJob: Job = Job(),
    private val serviceScope: CoroutineScope = CoroutineScope(Dispatchers.IO + serviceJob)
    ):DummyDownloadServiceWorkerInterface {

    override fun onStartDownloadAction() {
        onCreate()
        val notification = buildNotification(-1)
        if(startForegroundService(notification)){
            onStartDownload()
        }else{
            dummyDownloadToastHelper.noNotificationNotSupported()
        }
    }

    override fun startForegroundService(notification: Notification) :Boolean= foregroundServiceHelper.startForegroundService(notification)

    private fun onCreate() {
        downloadProgressNotifierClient.onCreate(dummyDownloadService.getContext())
    }



    override fun onStopDownloadAction() {
        dummyDownloadService.stopSelf()
        dummyDownloadService.stopForeground(STOP_FOREGROUND_REMOVE)
    }
    private fun onStartDownload() {
        serviceScope.launch {
            dummyDownloadHelper.download().collect{
                downloadProgressNotifierClient.onUpdate(it)
            }
            dummyDownloadService.stopSelf()
        }
    }

    override fun onCancel() {
        serviceJob.cancel()
    }
    fun buildNotification(progress: Int)=downloadProgressNotifierClient.buildNotification(progress)
}