package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import javax.inject.Inject

interface DownloadProgressBroadcastHelperInterface:DownloadProgressWatcher{
    fun broadcastProgress(context: Context, progress: Int)
}

class DownloadProgressBroadcastHelper @Inject constructor(): DownloadProgressBroadcastHelperInterface {
    override fun broadcastProgress(context: Context, progress: Int) {
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
            throw DownloadProgressWatcherNoContext()
        }
    }
}