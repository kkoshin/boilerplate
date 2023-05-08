package com.github.foodiestudio.application.common

actual fun logcat(msg: () -> String) {
    println(msg())
}