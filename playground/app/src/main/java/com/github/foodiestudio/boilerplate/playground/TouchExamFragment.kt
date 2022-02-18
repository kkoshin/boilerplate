package com.github.foodiestudio.boilerplate.playground

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.MotionEvent.AXIS_X
import android.view.MotionEvent.AXIS_Y
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.OverScroller
import androidx.annotation.RequiresApi
import androidx.core.view.ViewConfigurationCompat
import androidx.fragment.app.Fragment
import com.github.foodiestudio.boilerplate.playground.utils.log
import com.github.foodiestudio.sugar.toDp

/**
 * Touch 练习
 */
class TouchExamFragment : Fragment(R.layout.frag_touch_exam) {

    lateinit var target: ImageView

    private var startX: Float = 0F
    private var startY: Float = 0F

//    private var mSlop: Int = 0

    private val scroller: OverScroller by lazy { OverScroller(context) }

    private fun reset(x: Float, y: Float) {
        startX = x
        startY = y
    }

    private fun offset(x: Float, y: Float): Pair<Float, Float> {
        return (x - startX) to (y - startY)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        target = view.findViewById(R.id.iv_target)

//        mSlop = ViewConfiguration.get(context).scaledTouchSlop
        target.setOnTouchListener { v, event ->
            val mode = when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    reset(event.x, event.y)
//                    log(
//                        """
//                        已知
//                            ● ViewGroup：paddingTop = 2dp
//                                ○ ImageView: paddingTop = 4dp, marginTop = 8dp
//
//                        那么 ImageView 的
//                            top: ${v.top.toDp()}
//                            Y: ${v.y.toDp()}
//                            rotationX: ${v.rotationX}
//                            rotationY: ${v.rotationY}
//                    """.trimIndent()
//                    )
                    "down"
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    "other down"
                }
                MotionEvent.ACTION_MOVE -> {
                    translate(v, offset(event.x, event.y))
                    "move"
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    "up(still contain pointer)"
                }
                MotionEvent.ACTION_UP -> {
                    reset(0f, 0f)
//                    smoothBack(v)
                    "all_up"
                }
                else -> "NULL"
            }
            /**
             * x,y 是以这个 View 的左上角作为坐标轴中心，->Y
             * rawX, rawY 则是以最外面的 Window？的左上角作为坐标轴中心
             */
            event.apply {
                log(
                    """
                    =================
                    mode: $mode
                    x: ${x.toDp()}
                    y: ${y.toDp()}
                    rawX: ${rawX.toDp()}
                    rawY: ${rawY.toDp()}
                    Axis_X: ${getAxisValue(AXIS_X).toDp()}
                    Axis_Y: ${getAxisValue(AXIS_Y).toDp()}
                    =================
                """.trimIndent()
                )

            }
            mode == "down"
        }
    }

    /**
     * 顺滑的返回原处
     */
    private fun smoothBack(v: View) {
        v.animate()
            .translationX(0f)
            .translationY(0f)
            .scaleX(1f)
            .scaleX(1f)
    }

    private fun translate(v: View, offset: Pair<Float, Float>) {
        offset.apply {
            v.translationX += first
            v.translationY += second
        }
    }

    /**
     * 双指放大/缩小
     * 获取两个指针的大小，以及计算两个指针运动方向是相向还是相背，最后调整一下阈值也就是触发时的敏感值
     * [scaleOffset] 应该在 [0, 1]内
     */
    private fun zoom(v: View, scaleOffset: Float) {
        v.scaleX = 1 + scaleOffset
        v.scaleY = 1 + scaleOffset
    }

    /**
     *
     */
    fun rotate() {

    }

    companion object {
        fun newInstance(): TouchExamFragment {
            val args = Bundle()

            val fragment = TouchExamFragment()
            fragment.arguments = args
            return fragment
        }
    }
}