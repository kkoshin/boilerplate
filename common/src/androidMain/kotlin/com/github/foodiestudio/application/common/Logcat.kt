package com.github.foodiestudio.application.common

import logcat.logcat

actual fun logcat(msg: () -> String) {
    logcat("Logcat") { msg() }
}