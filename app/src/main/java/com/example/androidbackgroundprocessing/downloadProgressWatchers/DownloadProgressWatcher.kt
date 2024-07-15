package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.content.Context

interface DownloadProgressWatcher{
    fun onCreate(context: Context)
    fun onUpdate(progress:Int)
}