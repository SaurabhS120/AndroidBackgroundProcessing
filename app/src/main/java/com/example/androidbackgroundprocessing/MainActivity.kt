package com.example.androidbackgroundprocessing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var startButton: Button
    lateinit var stopButton: Button
    private val localReceiver = DownloadProgressBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.POST_NOTIFICATIONS"),0)
        }
        textView = findViewById(R.id.textView)
        startButton = findViewById(R.id.buttonStart)
        stopButton = findViewById(R.id.buttonStop)
        startButton.setOnClickListener {
            Intent(applicationContext,DummyDownloadService::class.java).also {
                it.action = DummyDownloadService.DownloadActions.START.toString()
                startService(it)
            }
        }
        stopButton.setOnClickListener {
            Intent(applicationContext,DummyDownloadService::class.java).also {
                it.action = DummyDownloadService.DownloadActions.STOP.toString()
                startService(it)
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, IntentFilter("download-progress"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver)
    }
    inner class DownloadProgressBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val progress:Int? = intent?.getIntExtra("progress",0)
            textView.text = when(progress){
                -1->"Download starting"
                100->"Download completed"
                -2->"Download cancelled"
                else->"Download Progress : $progress%"
            }
        }

    }
}