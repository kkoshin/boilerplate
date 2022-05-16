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
import com.github.foodiestudio.boilerplate.playground.utils.ScreenUtil
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

    private var mClockWise: Boolean? = null

    init {
        viewBinding.imageButtonClose.setOnClickListener {
            dismiss()
        }

//        viewBinding.imageButtonResize.setOnTouchListener(
//            SimpleDistanceChangeListener(
//                onChange = { dx, dy ->
//                    if (mClockWise == null) {
//                        return@SimpleDistanceChangeListener
//                    }
//                    resize(dx, dy, mClockWise!!)
//                }
//            )
//        )

        viewBinding.imageButtonResize.setOnTouchListener(
            SimpleDistanceChangeListener(
                onPreChange = {
                    // 临时扩大window范围，这样可以尽可能减少window边框更新
                    viewBinding.ll.updateLayoutParams {
                        width = mLayoutParams.width
                        height = mLayoutParams.height
                    }
                    mLayoutParams.width = ScreenUtil.getScreenSize(context).x - mLayoutParams.x
                    // FIXME 注意这里不能越界，不然会导致window的往上漂移
                    mLayoutParams.height =
                        ScreenUtil.getScreenSize(context).y - mLayoutParams.y
                    invalidateView()
                },
                onChangeEnd = {
                    mLayoutParams.width = viewBinding.ll.width
                    mLayoutParams.height = viewBinding.ll.height
                    invalidateView()
                },
                onChange = { dx, dy ->
                    logcat {
                        """before resize:
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
                    }
                    viewBinding.ll.updateLayoutParams {
                        width += dx.toInt()
                        height += dy.toInt()
                    }
                    logcat {
                        """after resize dx, dy: ${dx.toDp()}, ${dy.toDp()}
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
                    }
                })
        )

        viewBinding.btnXxx.setOnClickListener {
            val dx = 10.dp
            val dy = 0.dp
            logcat {
                """before click(window):
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
            }

            val v = viewBinding.ll
            logcat {
                """before click(view):
                    (x, y): ${v.x.toDp()}, ${v.y.toDp()}
                    width : ${v.width.toDp()}
                    height: ${v.height.toDp()}
                """.trimIndent()
            }
            resizeInLandscape(dx, dy, mClockWise ?: false)
//            v.updateLayoutParams {
//                width = mLayoutParams.width
//                height = mLayoutParams.height
//            }
//            mLayoutParams.width = ScreenUtil.getScreenSize(context).x - mLayoutParams.x
//            mLayoutParams.height =
//                ScreenUtil.getScreenSize(context).y - mLayoutParams.y - 50.dp.toInt()
//            invalidateView()
//            updateWindowSize(dx, dy)
            logcat {
                """after click(view):
                    (x, y): ${v.x.toDp()}, ${v.y.toDp()}
                    width : ${v.width.toDp()}
                    height: ${v.height.toDp()}
                """.trimIndent()
            }
            logcat {
                """after resize dx, dy: ${dx.toDp()}, ${dy.toDp()}
                    (x, y): ${mLayoutParams.x.toDp()}, ${mLayoutParams.y.toDp()}
                    width : ${mLayoutParams.width.toDp()}
                    height: ${mLayoutParams.height.toDp()}
                """.trimIndent()
            }
        }

        viewBinding.imageButtonRotate.setOnClickListener {
            rotate(true, false)
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

    private val maxWidth: Int by lazy { ScreenUtil.getScreenSize(context).x }
    private val maxHeight: Int by lazy { ScreenUtil.getScreenSize(context).y }

    private val leftSpace: Int
        get() = mLayoutParams.x

    private val topSpace: Int
        get() = mLayoutParams.y

    private val rightSpace: Int
        get() = maxWidth - leftSpace - mLayoutParams.width

    private val bottomSpace: Int
        get() = maxHeight - topSpace - mLayoutParams.height


    private fun resizeInLandscape(dx: Float, dy: Float, clockWise: Boolean = false) {
        if (clockWise) {
            mLayoutParams.y += dy.toInt()
            mLayoutParams.height -= dy.toInt()
            mLayoutParams.width += dx.toInt()
        } else {
            mLayoutParams.height += dy.toInt()
            mLayoutParams.x += dx.toInt()
            mLayoutParams.width -= dx.toInt()
        }
        viewBinding.ll.apply {
            val h_ = if (clockWise) dx else -dx
            val w_ = if (clockWise) -dy else dy
            translationY += ((w_ - h_) / 2).toInt()
            translationX += ((h_ - w_) / 2).toInt()
            updateLayoutParams {
                width += w_.toInt()
                height += h_.toInt()
            }
            invalidate()
        }
        invalidateView()
    }

    val lastWidth: Int = 0

    private fun rotate(archerToTop: Boolean = true, clockWise: Boolean = false) {
        mClockWise = clockWise
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
        mClockWise = null
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