package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DownloadProgressBroadcastHelper: DownloadProgressWatcher {
    fun broadcastProgress(context: Context, progress: Int) {
        val intent = Intent("download-progress").apply {
            putExtra("progress",progress)
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private  var context:Context? = null
    override fun onCreate(context: Context) {
        this.context = context
    }

    override fun onUpdate(progress: Int) {
        if(context != null){
            broadcastProgress(context!!,progress)
        }else{
            throw Exception("Context is null, onCreate was not called")
        }
    }
}