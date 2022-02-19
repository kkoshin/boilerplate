package com.github.foodiestudio.boilerplate.playground.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import com.github.foodiestudio.boilerplate.playground.utils.shortToast
import java.util.jar.Attributes

/**
 * 支持双指缩放等
 * TODO 待验证
 */
class PhotoView(
    context: Context,
    attributes: AttributeSet
) : AppCompatImageView(context, attributes) {

    private var scaleFactor = 1f

    private val detector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                context.shortToast("detect double tap.")
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                context.shortToast("detect onSingleTapConfirmed.")
                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                scrollBy(distanceX.toInt(), distanceY.toInt())
                return true
            }
        })

    private val scaleDetector = ScaleGestureDetector(context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(0.1f, 5.0f)
                invalidate()
                return true
            }
        }
    )

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return scaleDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            save()
            scale(scaleFactor, scaleFactor)
            super.onDraw(canvas)
            restore()
        }
    }
}