package com.example.androidbackgroundprocessing

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DownloadProgressBroadcastHelper{
    fun broadcastProgress(context: Context, progress: Int) {
        val intent = Intent("download-progress").apply {
            putExtra("progress",progress)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}