package com.example.androidbackgroundprocessing

import android.content.Context

class MultiDownloadProgressWatcher(val watchers: List<DownloadProgressWatcher>) : DownloadProgressWatcher{
    override fun onCreate(context: Context) {
        watchers.forEach { watcher->
            watcher.onCreate(context)
        }
    }

    override fun onUpdate(progress: Int) {
        watchers.forEach { watcher->
            watcher.onUpdate(progress)
        }
    }


}