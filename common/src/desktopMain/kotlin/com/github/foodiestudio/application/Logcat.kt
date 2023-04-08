package com.github.foodiestudio.application

actual fun logcat(msg: () -> String) {
    println(msg())
}