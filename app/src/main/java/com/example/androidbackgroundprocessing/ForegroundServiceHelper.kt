package com.example.androidbackgroundprocessing

import android.app.Notification
import android.content.pm.ServiceInfo
import android.os.Build

class ForegroundServiceHelper(val dummyDownloadService:DummyDownloadServiceInterface):ForegroundServiceHelperInterface {
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
}