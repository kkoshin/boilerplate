package com.github.foodiestudio.boilerplate.playground.aidl

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.github.foodiestudio.boilerplate.playground.IHelloService
import com.github.foodiestudio.boilerplate.playground.R
import com.github.foodiestudio.boilerplate.playground.utils.logcat
import com.github.foodiestudio.boilerplate.playground.utils.shortToast

class ClientActivity : ComponentActivity(R.layout.activity_client) {

    private var bound: Boolean = false

    private var remoteService: IHelloService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            shortToast("service connected")
            remoteService = IHelloService.Stub.asInterface(service)
            bound = true
            logcat {
                "get content:" + remoteService?.haha?.content
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            shortToast("service disconnected")
            remoteService = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.btn_bind).setOnClickListener {
            if (bound) {
                return@setOnClickListener
            }
            bindService(
                Intent(this, AidlRemoteService::class.java),
                connection,
                BIND_AUTO_CREATE
            )
        }
        findViewById<Button>(R.id.btn_unbind).setOnClickListener {
            if (!bound) {
                return@setOnClickListener
            }
            unbindService(connection)
        }
    }
}