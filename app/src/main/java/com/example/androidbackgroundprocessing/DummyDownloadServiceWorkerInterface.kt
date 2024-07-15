package com.example.androidbackgroundprocessing

import android.app.Notification
import android.content.Context

interface DummyDownloadServiceWorkerInterface {
    suspend fun onStartDownloadAction()
    fun onStopDownloadAction()
    fun startForegroundService(notification: Notification): Boolean
}