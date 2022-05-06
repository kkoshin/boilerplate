package com.github.foodiestudio.boilerplate.playground

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.view.*
import com.github.foodiestudio.boilerplate.playground.utils.ScreenUtil
import com.github.foodiestudio.boilerplate.playground.utils.logcat
import kotlin.math.abs

private fun IntArray.safeGet(position: Int): Int {
    return when {
        position > this.size -> {
            get(position / size)
        }
        position < 0 -> {
            if (position == -1) {
                assert(size - ((abs(position) + size) / size) == 3)
            }
            get(size - ((abs(position) + size) / size))
        }
        else -> {
            get(position)
        }
    }.also {
        logcat("NULL") { "value: $it" }
    }
}

class RotateActivity : ComponentActivity(R.layout.activity_rotate) {

    private val _100dp by lazy { ScreenUtil.getPX(this, 100f) }
    private val _200dp by lazy { ScreenUtil.getPX(this, 200f) }
    private val _50dp by lazy { ScreenUtil.getPX(this, 50f) }
    private val _16dp by lazy { ScreenUtil.getPX(this, 16f) }

    private val screenWidth by lazy { ScreenUtil.getScreenSize(this).x }

    private var marginsArray: IntArray = intArrayOf(1, 2, 3, 4) // top, end, bottom, start
    private var indexOffset = 0

    private val contentView by lazy { findViewById<View>(R.id.ll) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            rotate(true,false)
        }
        findViewById<Button>(R.id.btn_test_2).setOnClickListener {
            reset()
        }
        contentView.apply {
            marginsArray = intArrayOf(
                marginTop,
                marginEnd, //_16dp,
                marginBottom,//_50dp,
                marginStart,//_100dp
            )
        }
        indexOffset = 0
        reset()

        findViewById<Button>(R.id.btn_xxx).setOnClickListener {
            contentView.rotation = 270f
        }
    }

    private val currentMarginStart: Int
        get() = marginsArray.safeGet(indexOffset + 3)

    private val currentMarginTop: Int
        get() = marginsArray.safeGet(indexOffset)

    private val currentMarginEnd: Int
        get() = marginsArray.safeGet(indexOffset + 1)

    private val currentMarginBottom: Int
        get() = marginsArray.safeGet(indexOffset + 2)

    private fun reset() {
        indexOffset = 0
        contentView.apply {
            rotation = 0f
            translationX = 0f
            translationY = 0f
            layoutParams.apply {
                height = _200dp // ViewGroup.LayoutParams.WRAP_CONTENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
                (this as ViewGroup.MarginLayoutParams).setMargins(
                    currentMarginStart,
                    currentMarginTop,
                    currentMarginEnd,
                    currentMarginBottom,
                )
            }
            requestLayout()
        }
    }

    // 靠近底部的视图旋转，旋转后只有高会变化
//    private fun rotateTest(clockWise: Boolean) {
//        findViewById<View>(R.id.ll).apply {
//            val h = height // wrap_content
//            val w = width // match_parent
//            val rotatedH =
//                ScreenUtil.getScreenSize(this.context).x  // ViewGroup.LayoutParams.MATCH_PARENT
//            val offset = rotatedH - h
//            rotation = if (clockWise) 270f else 90f
//            val distanceX = ((w - h - offset) / 2).toFloat()
//            translationX = if (clockWise) distanceX else -distanceX
//            translationY = ((rotatedH - w) / 2).toFloat()
//            updateLayoutParams {
//                height = rotatedH // 这里模拟旋转后尺寸变化
//                width = w
//            }
//            invalidate()
//
//            ObjectAnimator.ofFloat(findViewById(R.id.btn_xxx), "rotation", -90f, 0f).apply {
//                duration = 1000
//                start()
//            }
//        }
//    }

    /**
     * #============phone===========##                       #
     * # ____________w_______________#                       #
     * #|           before          |#  = phone rotate =>    #         TODO
     * #h                           h#   (clockwise)         #
     * #|____________w______________|#                       #
     * #============================##                       #==========================#
     */
    // 为了方便理解，应该先理解为宽和高变了，也就是中心点变化了，然后再计算 translate 前后的中心点位置需要移动的距离
    /**
     * @param archerToTop 初始时视图是否更接近屏幕顶部
     */
    private fun rotate(archerToTop: Boolean, clockWise: Boolean) {
        // 因为视图原本的 h 不参与计算，以及 margin 也不会，所以在调整窗口大小以及位置的时候，尽量调整这些参数
        contentView.apply {
            val w = width

            val h_ = width      // 旋转后的高度
            val w_ = height     // 旋转后的宽度
            rotation = if (clockWise) 270f else 90f
            val distanceY = ((h_ - w_) / 2).toFloat()
            if (archerToTop) {
                translationY = -distanceY
            } else {
                val distanceX = ((w - h_) / 2).toFloat()
                translationX = if (clockWise) distanceX else -distanceX
                translationY = distanceY
            }

            updateLayoutParams {
                height = h_ // 这里模拟旋转后尺寸变化
                width = w_
            }
            invalidate()
            playRotationAnimation()
        }
    }

    private fun playRotationAnimation() {
        ObjectAnimator.ofFloat(findViewById<Button>(R.id.imageButton_close), "rotation", -60f, 0f).apply {
            duration = 300
            start()
        }
    }
}