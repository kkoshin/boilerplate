package com.example.nativelib.dev

import android.content.Context
import androidx.startup.Initializer

/**
 * 为了解决多个 content provider 初始化顺序混乱的问题
 */
class DevInitializer : Initializer<DevShortcut> {

    override fun create(context: Context): DevShortcut {
        DevShortcut.init(context)
        return DevShortcut
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}