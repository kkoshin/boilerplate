package com.example.motion

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.constraintlayout.motion.widget.MotionLayout

class MainActivity : ComponentActivity(R.layout.activity_main) {

    private lateinit var motionLayout: MotionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        motionLayout = findViewById(R.id.ml)
        findViewById<View>(R.id.play).setOnClickListener {
            toNextState()
        }
    }

    private fun toNextState() {
        when (motionLayout.currentState) {
            R.id.start -> motionLayout.transitionToEnd()
            R.id.end -> motionLayout.transitionToStart()
        }
    }
}