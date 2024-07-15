package com.example.androidbackgroundprocessing

import android.app.Notification
import android.content.Context

interface DummyDownloadServiceInterface {
    fun startForeground(i: Int, notification: Notification, i1: Int)
    fun stopSelf()
    fun stopForeground(stopForegroundRemove: Int)
    fun getContext(): Context
}