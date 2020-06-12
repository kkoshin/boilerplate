package com.example.widget.recycler.ktx

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.autoClear(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, LifecycleObserver {
        private var holder: T? = null

        init {
            this@autoClear.lifecycle.addObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun clear() {
            holder = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            return holder!!
        }

        override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
            holder = value
        }
    }
    
