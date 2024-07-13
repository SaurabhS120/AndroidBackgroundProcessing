package com.example.androidbackgroundprocessing

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DummyDownloadService : Service() {
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
            DownloadActions.START.toString()->{
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
                    Toast.makeText(
                        applicationContext,
                        "Foreground notification not supported by device",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            DownloadActions.STOP.toString() -> onStopDownload()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
    private fun onStartDownload() {
        serviceScope.launch {
            for (progress:Int in -1..100){
              updateNotification(progress)
              delay(1000L)
            }
            stopSelf()
        }
    }

    private fun onStopDownload() {
        stopSelf()
    }
    fun updateNotification(progress: Int){
        val notification = buildNotification(progress)
        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else{
                notify(1, notification)
            }
        }
    }
    val notificationBuilder = NotificationCompat
        .Builder(this, "download_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Dummy Download")
    fun buildNotification(progress: Int = 0) =
        notificationBuilder
        .setProgress(100, progress, progress==-1)
        .build()
}