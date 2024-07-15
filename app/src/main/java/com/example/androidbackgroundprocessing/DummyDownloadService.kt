package com.example.androidbackgroundprocessing

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadProgressNotifierClient
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelper
import com.example.androidbackgroundprocessing.downloadProgressWatchers.DownloadToastHelperInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DummyDownloadService: Service(),DummyDownloadServiceInterface {

    @Inject lateinit var dummyDownloadHelper : DummyDownloadHelper
    @Inject lateinit var downloadProgressNotifierClient: DownloadProgressNotifierClient
    val dummyDownloadToastHelperInterface: DownloadToastHelperInterface = DownloadToastHelper(this)
    val dummyDownloadServiceWorker: DummyDownloadServiceWorkerInterface by lazy {
        DummyDownloadServiceWorker(
            dummyDownloadHelper,
            downloadProgressNotifierClient,
            this,
            dummyDownloadToastHelperInterface
        )
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    enum class DownloadActions {
        START, STOP
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action.toString()) {
            DownloadActions.START.toString()-> dummyDownloadServiceWorker.onStartDownloadAction()
            DownloadActions.STOP.toString() -> dummyDownloadServiceWorker.onStopDownloadAction()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        dummyDownloadServiceWorker.onCancel()
    }

    override fun getContext(): Context {
        return this
    }

}