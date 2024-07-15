package com.example.androidbackgroundprocessing

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressNotifierClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DummyDownloadService: Service() {
    @Inject lateinit var dummyDownloadHelper : DummyDownloadHelper
    @Inject lateinit var downloadProgressNotifierClient: DownloadProgressNotifierClient
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    enum class DownloadActions {
        START, STOP
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action.toString()) {
            DownloadActions.START.toString()->onStartDownloadAction()
            DownloadActions.STOP.toString() -> onStopDownloadAction()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onStartDownloadAction() {
        downloadProgressNotifierClient.onCreate(this)
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
                } else {
                    0
                }
            )
            onStartDownload()
        } else {
            noNotificationNotSupported()
        }
    }

    private fun noNotificationNotSupported() {
        Toast.makeText(
            applicationContext,
            "Foreground notification not supported by device",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
    private fun onStartDownload() {
        serviceScope.launch {
            dummyDownloadHelper.download().collect{
                downloadProgressNotifierClient.onUpdate(it)
            }
            stopSelf()
        }
    }


    private fun onStopDownloadAction() {
        stopSelf()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    fun buildNotification(progress: Int = 0)=downloadProgressNotifierClient.buildNotification(progress)
}