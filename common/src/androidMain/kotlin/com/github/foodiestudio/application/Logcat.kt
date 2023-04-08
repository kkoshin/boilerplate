package com.github.foodiestudio.application

import logcat.logcat

actual fun logcat(msg: () -> String) {
    logcat("Logcat") { msg() }
}