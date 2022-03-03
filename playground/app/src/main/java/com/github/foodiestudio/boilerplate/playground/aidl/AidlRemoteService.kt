package com.github.foodiestudio.boilerplate.playground.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.foodiestudio.boilerplate.playground.Haha
import com.github.foodiestudio.boilerplate.playground.IHelloService
import com.github.foodiestudio.boilerplate.playground.utils.logcat

/**
 * 采用 aidl 的方式去创建 binder 对象
 */
class AidlRemoteService : Service() {
    var hey: Haha = Haha().apply {
        content = "嘿嘿嘿"
    }

    private val binder = object : IHelloService.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            logcat {
                "basicTypes invoke + $anInt"
            }
        }

        override fun getHaha(): Haha {
            return hey
        }
    }

    override fun onCreate() {
        super.onCreate()
        logcat {
            "RemoteService onCreate"
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        logcat {
            "RemoteService on bind"
        }
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        logcat {
            "RemoteService on Unbind"
        }
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        logcat {
            "RemoteService onDestroy"
        }
    }
}