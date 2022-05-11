package com.github.foodiestudio.boilerplate.playground.window

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMarginsRelative
import com.github.foodiestudio.boilerplate.playground.databinding.ContentDialogBinding
import com.github.foodiestudio.boilerplate.playground.databinding.DialogOverlayFooBinding
import com.github.foodiestudio.boilerplate.playground.utils.logcat
import com.github.foodiestudio.sugar.dp
import com.github.foodiestudio.sugar.toDp

@SuppressLint("ClickableViewAccessibility")
class FooWindow(
    context: Context,
    onClick: () -> Unit,
    initParams: WindowManager.LayoutParams.() -> Unit
) : OverlayWindow<DialogOverlayFooBinding>(
    context, DialogOverlayFooBinding.inflate(
        LayoutInflater.from(context)
    ), initParams
) {

    init {
        viewBinding.imageButtonClose.setOnClickListener {
            dismiss()
        }

        viewBinding.imageButtonResize.setOnTouchListener(SimpleDistanceChangeListener { dx, dy ->
            logcat {
                """before click:
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
            }
            updateWindowSize(dx, dy)
            logcat {
                """after resize dx, dy: ${dx.toDp()}, ${dy.toDp()}
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
            }
        })

        viewBinding.btnXxx.setOnClickListener {
            val dx = 10.dp
            val dy = 0.dp
            logcat {
                """before click:
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
            }
            updateWindowSize(dx, dy)
            logcat {
                """after resize dx, dy: ${dx.toDp()}, ${dy.toDp()}
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
            }
        }

        viewBinding.imageButtonRotate.setOnClickListener {
            rotate()
//            viewBinding.ll.updateLayoutParams {
//                height = 400.dp.toInt()
//            }
        }


        viewBinding.imageButtonReset.setOnClickListener {
            reset()
////            viewBinding.ll.rotation = 0f
////            viewBinding.ll.translationX = 0f
//            viewBinding.ll.updateLayoutParams {
//                height = 500.dp.toInt()
//            }
        }
    }

    val lastWidth : Int = 0

    private fun rotate(archerToTop: Boolean = true, clockWise: Boolean = false) {
        // 因为视图原本的 h 不参与计算，以及 margin 也不会，所以在调整窗口大小以及位置的时候，尽量调整这些参数
        val contentView = viewBinding.ll
        contentView.apply {
            val w = width
            val h_ = width      // 旋转后的高度
            val w_ = height     // 旋转后的宽度
            rotation = if (clockWise) 270f else 90f
            if (archerToTop) {
                translationY += -((h_ - w_) / 2).toFloat()
                translationX += -((w_ - h_) / 2).toFloat()
            } else {
                val distanceX = ((w - h_) / 2).toFloat()
                translationX += if (clockWise) distanceX else -distanceX
                translationY += ((h_ - w_) / 2).toFloat()
            }

            updateLayoutParams {
                height = h_ // 这里模拟旋转后尺寸变化
                width = w_
            }
            invalidate()
        }
    }

    private fun reset() {
        viewBinding.ll.apply {
//            val h_ = width
//            val w_ = height
            rotation = 0f
            translationX = 0f
            translationY = 0f
            updateLayoutParams {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            invalidate()
        }
    }
}