package com.example.nativelib.dev

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.nativelib.R

object DevShortcut {

    fun init(context: Context) {
        val shortcut = ShortcutInfoCompat.Builder(context, "id-dev-tools")
            .setShortLabel("Dev Tools")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_dev_tools_shotcut))
            .setIntent(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://movie.douban.com/"))
            )
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }
}