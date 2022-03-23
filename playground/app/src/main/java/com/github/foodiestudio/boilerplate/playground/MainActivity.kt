package com.github.foodiestudio.boilerplate.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.example.nativelib.NativeLibWrapper
import com.github.foodiestudio.boilerplate.playground.utils.logcat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logcat {
            NativeLibWrapper().stringFromJNI()
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.content, TouchExamFragment.newInstance())
            .commit()
    }
}