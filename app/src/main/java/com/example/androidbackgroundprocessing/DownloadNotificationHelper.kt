package com.example.androidbackgroundprocessing

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class DownloadNotificationHelper{
    private var notificationBuilder : NotificationCompat.Builder? = null
    fun buildNotification(context: Context, progress: Int = 0): Notification {
        if(notificationBuilder==null){

            val notificationCancelIntent: Intent =
                Intent(context, DummyDownloadService::class.java).apply {
                    action = DummyDownloadService.DownloadActions.STOP.toString()
                }
            val notificationCancelPendingIntent: PendingIntent = PendingIntent.getService(
                context,
                0,
                notificationCancelIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            val notificationCancelAction =
                NotificationCompat.Action(null, "Cancel", notificationCancelPendingIntent)

            notificationBuilder = NotificationCompat.Builder(context, "download_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Dummy Download")
                .addAction(notificationCancelAction)
        }
        return  notificationBuilder!!
            .setProgress(100, progress, progress == -1)
            .build()
    }
    fun updateNotification(applicationContext: Context, progress: Int){
        val notification = buildNotification(applicationContext,progress)
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
}