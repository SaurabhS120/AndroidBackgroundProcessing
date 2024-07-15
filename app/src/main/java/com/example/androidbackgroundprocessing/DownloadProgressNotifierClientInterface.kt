package com.example.androidbackgroundprocessing

import android.app.Notification
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressWatcher

interface DownloadProgressNotifierClientInterface: DownloadProgressWatcher {
    fun buildNotification(progress: Int = 0):Notification
}