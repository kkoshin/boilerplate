package com.github.foodiestudio.boilerplate.playground.utils

import android.content.Context
import android.widget.Toast

fun Context.shortToast(msg: CharSequence) = toast { msg }

fun Context.toast(shortToast: Boolean = true, msg: () -> CharSequence) {
    Toast.makeText(this, msg(), if (shortToast) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}