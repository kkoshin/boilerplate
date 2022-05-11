package com.github.foodiestudio.boilerplate.playground

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.MotionEvent.AXIS_X
import android.view.MotionEvent.AXIS_Y
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.OverScroller
import androidx.fragment.app.Fragment
import com.github.foodiestudio.boilerplate.playground.databinding.ContentDialogBinding
import com.github.foodiestudio.boilerplate.playground.databinding.DialogOverlayFooBinding
import com.github.foodiestudio.boilerplate.playground.utils.IOverlayDialog
import com.github.foodiestudio.boilerplate.playground.utils.OverlayWindows
import com.github.foodiestudio.boilerplate.playground.utils.logcat
import com.github.foodiestudio.boilerplate.playground.utils.toast
import com.github.foodiestudio.sugar.dp
import com.github.foodiestudio.sugar.toDp

/**
 * Touch 练习
 */
class TouchExamFragment : Fragment(R.layout.frag_touch_exam) {

    private lateinit var target: ImageView
    private lateinit var pvDetector: ImageView

    private lateinit var cb: CheckBox
    private lateinit var ll: LinearLayout

    private var startX: Float = 0F
    private var startY: Float = 0F


    private val scroller: OverScroller by lazy { OverScroller(context) }

    private val dialog: IOverlayDialog by lazy {
        OverlayWindows.createFoo(requireContext(), onClick = {

        }) {
            x = 20.dp.toInt()
            y = 100.dp.toInt()
            width = 350.dp.toInt()
            height = 500.dp.toInt()
        }
    }

    private fun reset(x: Float, y: Float) {
        startX = x
        startY = y
    }

    private fun offset(x: Float, y: Float): Pair<Float, Float> {
        return (x - startX) to (y - startY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        target = view.findViewById(R.id.iv_target)
        ll = view.findViewById(R.id.ll_touch_delegate)
        cb = view.findViewById(R.id.cb)
        pvDetector = view.findViewById(R.id.pv)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            setupTouchDelegate()
        }

        pvDetector.setOnClickListener {
            PhotoPreviewActivity.start(
                requireContext(), ActivityOptions
                    .makeSceneTransitionAnimation(activity, pvDetector, "007")
            )
        }

        showDialog()
//        setupCustomTouchEvent()
    }

    private fun showDialog() {
        target.setOnClickListener {
            it.context.toast { "show overlay dialog" }
            dialog.show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCustomTouchEvent() {
        target.setOnTouchListener { v, event ->
            val mode = when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    reset(event.x, event.y)
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
                logcat {
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
                }

            }
            mode == "down"
        }
    }

    private fun setupTouchDelegate() {
        logcat {
            """
                ll.top: ${ll.clipBounds?.top}
                ll.bottom: ${ll.clipBounds?.bottom}
            """.trimIndent()
        }
        val rect = Rect(ll.left, ll.top, ll.right, ll.bottom)
        ll.touchDelegate = TouchDelegate(rect, cb)
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