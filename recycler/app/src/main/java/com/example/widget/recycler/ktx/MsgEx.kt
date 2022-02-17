package com.example.widget.recycler.ktx

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(msg: CharSequence, @Snackbar.Duration duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, duration).show()
}