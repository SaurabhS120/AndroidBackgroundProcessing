package com.example.androidbackgroundprocessing

import android.app.Notification
import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.pm.ServiceInfo
import android.os.Build
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressNotifierClient
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelperInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DummyDownloadServiceWorker(
    val dummyDownloadHelper : DummyDownloadHelper,
    val downloadProgressNotifierClient: DownloadProgressNotifierClient,
    val dummyDownloadService: DummyDownloadServiceInterface,
    val dummyDownloadToastHelper: DownloadToastHelperInterface
    ):DummyDownloadServiceWorkerInterface {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartDownloadAction() {
        onCreate()
        val notification = buildNotification()
        if(startForegroundService(notification)){
            onStartDownload()
        }else{
            dummyDownloadToastHelper.noNotificationNotSupported()
        }
    }

    override fun startForegroundService(notification: Notification) :Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dummyDownloadService.startForeground(
                1,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
                } else {
                    0
                }
            )
            return true
        } else {
            return false
        }
    }

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
    fun buildNotification(progress: Int = 0)=downloadProgressNotifierClient.buildNotification(progress)
}