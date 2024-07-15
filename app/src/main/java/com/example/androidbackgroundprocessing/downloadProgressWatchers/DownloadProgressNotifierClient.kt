package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.app.Notification
import android.content.Context
import com.example.androidbackgroundprocessing.DownloadProgressNotifierClientInterface
import javax.inject.Inject

class DownloadProgressNotifierClient @Inject constructor(
    private val downloadBroadcastHelper : DownloadProgressBroadcastHelperInterface,
    private val downloadNotificationHelper : DownloadNotificationHelperInterface,
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

    override fun onCancel() {
        ifContextAvailable(context){
            downloadWatcher.onCancel()
        }
    }

    private fun <T> withContext(context:Context?, action:(context:Context)->T):T{
        if(context == null){
            throw DownloadProgressWatcherNoContext()
        }else{
            return action(context)
        }
    }

    private fun <T> ifContextAvailable(context:Context?, action:(context:Context)->T){
        if(context != null){
            action(context)
        }
    }
}