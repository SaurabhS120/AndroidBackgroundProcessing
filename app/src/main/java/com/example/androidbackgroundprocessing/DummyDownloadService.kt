package com.example.androidbackgroundprocessing

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class DummyDownloadService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    enum class DownloadActions {
        START, STOP
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action.toString()) {
            DownloadActions.START.toString() -> onStartDownload()
            DownloadActions.STOP.toString() -> onStopDownload()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onStartDownload() {
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
        } else {
            Toast.makeText(
                applicationContext,
                "Foreground notification not supported by device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onStopDownload() {
        stopSelf()
    }

    fun buildNotification(progress: Int = 0) = NotificationCompat
        .Builder(this, "download_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Dummy Download")
        .setProgress(100, progress, false)
        .build()
}