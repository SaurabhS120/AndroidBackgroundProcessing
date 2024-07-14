package com.example.androidbackgroundprocessing

import android.content.Context

interface DownloadProgressWatcher{
    fun onCreate(context: Context)
    fun onUpdate(progress:Int)
}