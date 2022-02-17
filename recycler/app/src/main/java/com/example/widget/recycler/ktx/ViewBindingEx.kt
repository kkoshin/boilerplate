package com.example.widget.recycler.ktx

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.autoUnbind(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, LifecycleObserver {
        private var holder: T? = null

        private var viewLifecycleOwner: LifecycleOwner? = null

        init {
            // Observe the View Lifecycle of the Fragment
            this@autoUnbind
                .viewLifecycleOwnerLiveData
                .observe(this@autoUnbind, Observer { newLifecycleOwner ->
                    viewLifecycleOwner
                        ?.lifecycle
                        ?.removeObserver(this)

                    viewLifecycleOwner = newLifecycleOwner.also {
                        it.lifecycle.addObserver(this)
                    }
                })
        }

//        init {
//            // 这里直接调用 viewLifecycleOwner 会报错，因为在 onCreateView 之前调用，此时的 getView 返回的为 null
//            this@autoClear.lifecycle.addObserver(this)
//        }

        /**
         * 一般是关注于 Fragment 的 onDestroyView，而不是 onDestroy
         * 前者需要用 viewLifecycleOwner.lifecycle, 后者则是直接 lifecycle
         */
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
    
