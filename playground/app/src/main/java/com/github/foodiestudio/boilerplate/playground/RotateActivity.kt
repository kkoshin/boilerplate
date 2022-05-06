package com.github.foodiestudio.boilerplate.playground

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.view.updateLayoutParams
import com.github.foodiestudio.boilerplate.playground.utils.ScreenUtil

class RotateActivity : ComponentActivity(R.layout.activity_rotate) {

    private val _200dp by lazy { ScreenUtil.getPX(this, 200f) }
    private val _300dp by lazy { ScreenUtil.getPX(this, 300f) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            rotateTest2(true)
        }
        findViewById<Button>(R.id.btn_test_2).setOnClickListener {
            reset()
        }

        findViewById<Button>(R.id.btn_xxx).setOnClickListener {
            findViewById<View>(R.id.ll).rotation = 270f
        }
    }

    private fun reset() {
        findViewById<View>(R.id.ll).apply {
            val rotatedH = ViewGroup.LayoutParams.WRAP_CONTENT
            rotation = 0f
            translationX = 0f
            translationY = 0f
            layoutParams.apply {
                height = rotatedH
            }
            requestLayout()
        }
    }

    // work right
    private fun rotateTest(clockWise: Boolean) {
        findViewById<View>(R.id.ll).apply {
            val h = height // wrap_content
            val w = width // match_parent
            val rotatedH =
                ScreenUtil.getScreenSize(this.context).x  // ViewGroup.LayoutParams.MATCH_PARENT
            val offset = rotatedH - h
            rotation = if (clockWise) 270f else 90f
            val distanceX = ((w - h - offset) / 2).toFloat()
            translationX = if (clockWise) distanceX else -distanceX
            translationY = ((rotatedH - w) / 2).toFloat()
            updateLayoutParams {
                height = rotatedH // 这里模拟旋转后尺寸变化
                width = w
                // 这里只需要旋转下margin的值就可以，如果需要处理margin
//                (this as ViewGroup.MarginLayoutParams).updateMargins(bottom = ScreenUtil.getPX(this@RotateActivity, 16f))
            }
            invalidate()

            ObjectAnimator.ofFloat(findViewById(R.id.btn_xxx), "rotation", -90f, 0f).apply {
                duration = 1000
                start()
            }
        }
    }

    /**
     * #============phone===========##                       #
     * # ____________w_______________#                       #
     * #|           before          |#  = phone rotate =>    #
     * #h                           h#   (clockwise)         #
     * #|____________w______________|#                       #
     * #============================##                       #==========================#
     */
    // 为了方便理解，应该先理解为宽和高变了，也就是中心点变化了，然后再计算 translate 前后的中心点位置需要移动的距离
    private fun rotateTest2(clockWise: Boolean) {
        findViewById<View>(R.id.ll).apply {
            val h = height // wrap_content
            val w = width // match_parent
            val h_ = width
            val w_ = width
            updateLayoutParams {
                height = h_ // 这里模拟旋转后尺寸变化
                width = w_
            }
            rotation = if (clockWise) 270f else 90f
            val distanceX = (w - (h_ / 2) - (w / 2)).toFloat()
            translationX = if (clockWise) distanceX else -distanceX
            translationY = ((h_ - w_) / 2).toFloat()
            invalidate()
        }
    }

    // 简洁版本，没有offset的计算
    private fun rotate(clockWise: Boolean) {
        findViewById<View>(R.id.ll).apply {
            val h_ = width
            val w_ = width
            rotation = if (clockWise) 270f else 90f
            val distanceX = ((w_ - h_)/ 2).toFloat()
            translationX = if (clockWise) distanceX else -distanceX
            translationY = ((h_ - w_) / 2).toFloat()
            updateLayoutParams {
                height = h_ // 这里模拟旋转后尺寸变化
                width = w_
            }
            invalidate()
        }
    }
}