package com.github.foodiestudio.boilerplate.playground.window

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.github.foodiestudio.boilerplate.playground.utils.IOverlayDialog

open class OverlayWindow<T : ViewBinding>(
    context: Context,
    protected val viewBinding: T,
    initParams: WindowManager.LayoutParams.() -> Unit = {}
) : IOverlayDialog {
    private val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    override var isShowing: Boolean = false

    private var isAdded: Boolean = false

    protected val mLayoutParams = WindowManager.LayoutParams().apply {
        gravity = Gravity.TOP or Gravity.START
        x = 0
        y = 0
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        format = PixelFormat.TRANSLUCENT
        dimAmount = 0.8f
    }

    init {
        initParams(mLayoutParams)
    }

    override fun hide() {
        viewBinding.root.isGone = true
        isShowing = false
    }

    override fun show() {
        if (isAdded) {
            if (!isShowing) {
                viewBinding.root.isVisible = true
            }
        } else {
            mWindowManager.addView(viewBinding.root, mLayoutParams)
            isAdded = true
        }
        isShowing = true
    }

    protected fun updateWindowOrientation(extra: WindowManager.LayoutParams.() -> Unit) {
        if (!isShowing) {
            return
        }
        extra(mLayoutParams)

        val w = mLayoutParams.width
        val h = mLayoutParams.height
        mLayoutParams.height = w
        mLayoutParams.width = h
        mWindowManager.updateViewLayout(viewBinding.root, mLayoutParams)
    }

    protected fun updateWindowPosition(deltaX: Float, deltaY: Float) {
        mLayoutParams.x = mLayoutParams.x + deltaX.toInt()
        mLayoutParams.y = mLayoutParams.y + deltaY.toInt()
        mWindowManager.updateViewLayout(viewBinding.root, mLayoutParams)
    }

    protected fun updateWindowSize(deltaX: Float, deltaY: Float) {
        val needUpdateWidth = validateWindowWidth(mLayoutParams.width + deltaX.toInt()).also {
            if (it) {
                mLayoutParams.width = mLayoutParams.width + deltaX.toInt()
            }
        }
        val needUpdateHeight = validateWindowHeight(mLayoutParams.height + deltaY.toInt()).also {
            if (it) {
                mLayoutParams.height = mLayoutParams.height + deltaY.toInt()
            }
        }
        if (needUpdateWidth || needUpdateHeight) {
            mWindowManager.updateViewLayout(viewBinding.root, mLayoutParams)
        }
    }

    protected fun invalidateView() {
        mWindowManager.updateViewLayout(viewBinding.root, mLayoutParams)
    }

    protected open fun validateWindowWidth(newWidth: Int): Boolean {
        return true
    }

    protected open fun validateWindowHeight(newHeight: Int): Boolean {
        return true
    }

    protected fun getCurrentBound(): Rect {
        return Rect(
            mLayoutParams.x,
            mLayoutParams.y,
            mLayoutParams.x + mLayoutParams.width,
            mLayoutParams.y + mLayoutParams.height
        )
    }

    override fun dismiss() {
        mWindowManager.removeViewImmediate(viewBinding.root)
        viewBinding.root.isVisible = true
        isShowing = false
        isAdded = false
        onDismiss()
    }

    protected open fun onDismiss() {
    }

    override fun cancel() {
        if (isAdded) {
            dismiss()
        }
    }
}