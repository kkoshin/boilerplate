package com.github.foodiestudio.boilerplate.playground.utils

import android.util.Log


const val TAG = "NULL"

fun logcat(tag: String = TAG, msg: () -> String?) {
    msg.invoke()?.let {
        Log.d(tag, it)
    }
}