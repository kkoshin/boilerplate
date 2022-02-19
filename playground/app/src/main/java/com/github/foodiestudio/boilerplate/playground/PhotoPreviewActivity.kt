package com.github.foodiestudio.boilerplate.playground

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * Activity 共享元素过渡动画
 */
class PhotoPreviewActivity : ComponentActivity(R.layout.activity_photo_preview) {

    companion object {
        @JvmStatic
        fun start(context: Context, activityOptions: ActivityOptions) {
            val starter = Intent(context, PhotoPreviewActivity::class.java)
            context.startActivity(starter, activityOptions.toBundle())
        }
    }
}