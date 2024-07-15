package com.example.androidbackgroundprocessing.downloadProgressWatchers

import android.content.Context
import android.widget.Toast

class DownloadToastHelper(val context: Context):DownloadToastHelperInterface {
    override fun noNotificationNotSupported() {
        Toast.makeText(
            context,
            "Foreground notification not supported by device",
            Toast.LENGTH_SHORT
        ).show()
    }
}