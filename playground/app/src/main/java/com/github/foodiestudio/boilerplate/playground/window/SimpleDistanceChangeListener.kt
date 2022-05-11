package com.github.foodiestudio.boilerplate.playground.window

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

internal class SimpleDistanceChangeListener(
    private val onChangeEnd: () -> Unit = {},
    private val onPreChange: () -> Unit = {},
    private val onChange: (Float, Float) -> Unit
) : View.OnTouchListener {
    private var downX = 0f
    private var downY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = motionEvent.rawX
                downY = motionEvent.rawY
                onPreChange()
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = motionEvent.rawX - downX
                val deltaY = motionEvent.rawY - downY
                downX = motionEvent.rawX
                downY = motionEvent.rawY
                onChange(deltaX, deltaY)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                onChangeEnd()
            }
        }
        return true
    }
}