package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.app.Notification
import android.content.Context
import com.example.androidbackgroundprocessing.DownloadProgressNotifierClientInterface
import javax.inject.Inject

class DownloadProgressNotifierClient @Inject constructor(
    private val downloadBroadcastHelper : DownloadProgressBroadcastHelper,
    private val downloadNotificationHelper : DownloadNotificationHelper,
    ): DownloadProgressNotifierClientInterface {
    private val downloadWatcher = MultiDownloadProgressWatcher(
        listOf(
            downloadNotificationHelper,
            downloadBroadcastHelper,
        )
    )

    private var context : Context? = null

    override fun buildNotification(progress: Int): Notification {
        return withContext(context){context: Context ->
            return@withContext downloadNotificationHelper.buildNotification(context,progress)
        }
    }

    override fun onCreate(context: Context) {
        withContext(context){context:Context->
            this.context = context
            downloadWatcher.onCreate(context)
        }
    }

    override fun onUpdate(progress: Int) {
        withContext(context){
            downloadWatcher.onUpdate(progress)
        }
    }

    private fun <T> withContext(context:Context?, action:(context:Context)->T):T{
        if(context == null){
            throw Exception("Context should not be null, onCreate should be called before")
        }else{
            return action(context)
        }
    }
}