package com.github.foodiestudio.boilerplate.playground.utils

import android.content.Context
import android.view.WindowManager
import com.github.foodiestudio.boilerplate.playground.window.FooWindow

object OverlayWindows {

    fun createFoo(
        context: Context,
        onClick: () -> Unit,
        initParams: WindowManager.LayoutParams.() -> Unit
    ): IOverlayDialog {
        return FooWindow(context, onClick, initParams)
    }
}

interface IOverlayDialog {
    var isShowing: Boolean

    fun show()

    fun hide()

    fun dismiss()

    fun cancel()
}

