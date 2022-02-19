package com.github.foodiestudio.boilerplate.playground.utils

import android.content.Context
import android.widget.Toast

fun Context.shortToast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}