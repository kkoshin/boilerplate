package com.github.foodiestudio.boilerplate.playground

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.view.*
import com.github.foodiestudio.boilerplate.playground.utils.ScreenUtil
import com.github.foodiestudio.boilerplate.playground.utils.logcat
import com.github.foodiestudio.sugar.dp
import com.github.foodiestudio.sugar.toDp
import kotlin.math.abs
import kotlin.math.roundToInt

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
    private val screenHeight by lazy { ScreenUtil.getScreenSize(this).y }

    private var marginsArray: IntArray = intArrayOf(1, 2, 3, 4) // top, end, bottom, start
    private var indexOffset = 0

    private val contentView by lazy { findViewById<View>(R.id.ll) }
    private val moveButton by lazy { findViewById<View>(R.id.imageButton_move) }
    private val resizeButton by lazy { findViewById<View>(R.id.imageButton_resize) }

    private var rotated = false

    private var offsetX = 0f
    private var offsetY = 0f

    private var deltaMarginLeft = 0f

    private val mClockWise = false

    private val width = 302.dp.toInt()
    private val height = 200.dp.toInt()

    private val initRect: Rect = Rect().apply {
        left = 30.dp.toInt()
        top = 100.dp.toInt()
        right = 30.dp.toInt() + width
        bottom = 100.dp.toInt() + height
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            rotate(true, mClockWise, contentView.width, contentView.height)
            rotated = true
        }
        findViewById<Button>(R.id.btn_test_2).setOnClickListener {
            reset()
            rotated = false
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

        findViewById<Button>(R.id.btn_xxx).setOnClickListener {
//            contentView.updateLayoutParams {
//                if (rotated) {
//                    val dX = if (mClockWise) _16dp else -_16dp
//                    deltaMarginLeft += dX
//                    height += dX
//                    contentView.translationX -= dX / 2
//                    contentView.translationY -= dX / 2
//                }
//            }

            with(contentView) {
                logcat("NULL") {
                    """
                    bounds:
                    width: ${width.toDp()},
                    height: ${height.toDp()}
                    x, y: ${x.toDp()}, ${y.toDp()}
                    translateX, translateY: ${translationX.toDp()}, ${translationY.toDp()} 
                    left, top : ${left.toDp()}, ${top.toDp()}
                    right, bottom : ${right.toDp()}, ${bottom.toDp()}
                    ===========================
                    offsetX: ${offsetX.toDp()}
                    offsetY: ${offsetY.toDp()}
                    ===========================
                """.trimIndent()
                }
            }

//            findViewById<View>(R.id.view_guideline).apply {
//                val rect = Rect()
//                getDrawingRect(rect)
//                val locationInWindow = IntArray(2)
//                getLocationInWindow(locationInWindow)
//                with(rect) {
//                    logcat("NULL") {
//                        """
//                    bounds:
//                    width: ${width.toDp()},
//                    height: ${height.toDp()}
//                    x, y: ${x.toDp()}, ${y.toDp()}
//                    left, top : ${left.toDp()}, ${top.toDp()}
//                    right, bottom : ${right.toDp()}, ${bottom.toDp()}
//                """.trimIndent()
//                    }
//                    logcat("NULL") {
//                        """
//                    location:
//                     x, y: ${locationInWindow[0].toDp()}, ${locationInWindow[1].toDp()}
//                """.trimIndent()
//                    }
//                }
//            }
        }

        resizeButton.setOnTouchListener(SimpleDistanceChangeListener(onChange = { dx, dy ->
            resize(dx, dy, mClockWise)
        }))
        moveButton.setOnTouchListener(SimpleDistanceChangeListener(onChange = { deltaX, deltaY ->
            val maxWidth = screenWidth
            val maxHeight = screenHeight

            if (!rotated) {
                val leftSpace = contentView.x
                val rightSpace = maxWidth - contentView.x - contentView.width
                val topSpace = contentView.y
                val bottomSpace = maxHeight - contentView.y - contentView.height
                move(
                    deltaX.coerceIn(-leftSpace, rightSpace),
                    deltaY.coerceIn(-topSpace, bottomSpace)
                )
            } else {
                if (mClockWise) {
                    // todo 类似计算的方式
//                    move(
//                        deltaX.coerceIn(-topSpace, bottomSpace),
//                        deltaY.coerceIn(-rightSpace, leftSpace)
//                    )
                } else {
                    // TODO: 发生缩放后，边界判断还是有点问题
                    val x_ = contentView.x + (width + height) / 2
                    val y_ = contentView.y + (width - height) / 2
                    val rightSpace = maxWidth - x_
                    val leftSpace = maxWidth - rightSpace - contentView.height
                    val topSpace = y_
                    val bottomSpace = maxHeight - topSpace - contentView.width

                    move(
                        deltaX.coerceIn(-leftSpace, rightSpace),
                        deltaY.coerceIn(-topSpace, bottomSpace)
                    )
                }

            }
        }))

        initSize()
    }

    private fun initSize() {
        contentView.updateLayoutParams {
            width = 0
            height = initRect.height()
            (this as ViewGroup.MarginLayoutParams).updateMarginsRelative(
                top = initRect.top,
                start = initRect.left,
                end = screenWidth - initRect.right,
                bottom = screenHeight - initRect.bottom
            )
        }
        contentView.invalidate()
    }

    private fun move(dx: Float, dy: Float) {
        offsetX += dx
        offsetY += dy
        contentView.apply {
            translationX += dx
            translationY += dy
        }
    }

    /**
     * 只需要处理一种横屏的情况，另一边其实差不多的情况，最多注意下垂直方向
     * - 视觉上的缩放大小
     *   1. 调整高度
     *      - 默认：可以
     *      - 旋转后：实际上对应的是调整宽度，然后需要加一定的translation 做抵消，因为中心点变了
     *   2. 调整宽度,默认是match_parent
     *      - 默认：调整 marginRight 为主
     *      - 旋转后：调整 height,然后加上 translation 做补偿
     */
    // TODO(Jiangc): clockWise 为 true 的情况下还需要取反
    private fun resize(deltaX: Float, deltaY: Float, clockWise: Boolean = false) {
        val max = contentView.width - contentView.marginStart
        contentView.updateLayoutParams {
            if (rotated) {
                width += deltaY.roundToInt()
                contentView.translationY += deltaY / 2

                val dX = if (clockWise) deltaX else -deltaX
                height += dX.roundToInt()
                contentView.translationX -= dX / 2
                contentView.translationY -= dX / 2
                // 竖屏情况下，宽度match_parent,需要通过修改对应的 margin 来达到对应的视觉效果
                deltaMarginLeft += dX.roundToInt()
            } else {
                height += deltaY.roundToInt()
                // 这里要用 updateMarginsRelative，不能是 updateMargins
                (this as ViewGroup.MarginLayoutParams).updateMarginsRelative(
                    end = (rightMargin - deltaX.roundToInt()).coerceIn(0, max)
                )
            }
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

    // TODO(Jiangc): 横屏 resize 之后，在恢复，框的大小有偏差
    private fun reset() {
        indexOffset = 0
        contentView.apply {
            rotation = 0f
            translationX = offsetX
            translationY = offsetY
            updateLayoutParams {
                height = width // ViewGroup.LayoutParams.WRAP_CONTENT
                width = 0 // constraint 约束
                (this as ViewGroup.MarginLayoutParams).updateMarginsRelative(
                    start = leftMargin - deltaMarginLeft.toInt()
                )
            }
            invalidate()
        }
        offsetX = 0f
        offsetY = 0f
        deltaMarginLeft = 0f
    }

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
    private fun rotate(archerToTop: Boolean, clockWise: Boolean, h_: Int, w_: Int) {
        // 因为视图原本的 h 不参与计算，以及 margin 也不会，所以在调整窗口大小以及位置的时候，尽量调整这些参数
        contentView.apply {
            val w = width

//            val h_ = width      // 旋转后的高度
//            val w_ = height     // 旋转后的宽度
            rotation = if (clockWise) 270f else 90f
            val distanceY = ((h_ - w_) / 2).toFloat()
            if (archerToTop) {
                translationY += -distanceY
//                // 如果constraintHorizontal_bias="0"时，居左的话，需要加这个偏移
//                translationX += -((w_ - h_) / 2).toFloat()
            } else {
                val distanceX = ((w - h_) / 2).toFloat()
                translationX += if (clockWise) distanceX else -distanceX
                translationY += distanceY
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
        ObjectAnimator.ofFloat(findViewById<Button>(R.id.imageButton_close), "rotation", -60f, 0f)
            .apply {
                duration = 300
                start()
            }
    }
}

private class SimpleDistanceChangeListener(
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
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = motionEvent.rawX - downX
                val deltaY = motionEvent.rawY - downY
                downX = motionEvent.rawX
                downY = motionEvent.rawY
                onChange(deltaX, deltaY)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}