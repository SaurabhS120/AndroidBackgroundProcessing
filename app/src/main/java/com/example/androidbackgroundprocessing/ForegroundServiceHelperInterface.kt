package com.example.androidbackgroundprocessing

import android.app.Notification

interface ForegroundServiceHelperInterface {
    fun startForegroundService(notification: Notification): Boolean
}