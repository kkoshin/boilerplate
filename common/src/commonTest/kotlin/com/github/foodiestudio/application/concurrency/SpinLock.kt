package com.github.foodiestudio.application.concurrency

import kotlinx.atomicfu.atomic

/**
 * 用户态的自旋锁，仅用于验证理论，实际不推荐自己来造轮子，直接使用 atomicfu
 * 算是一个最简单的锁实现，内部实现依赖于 atomic 指令的支持
 */
class SpinLock {

    // 使用 atomic 指令可以避免编译器优化，所以这里不需要额外的声明 volatile
    private var locked = atomic(false)

    fun withSpinLock(block: () -> Unit) {
        lock()
        try {
            block()
        } finally {
            unlock()
        }
    }

    private fun lock() {
        while (!locked.compareAndSet(expect = false, update = true)) {
            // 开始自旋，在计算资源比较充足的情况下，性能会极速下降
            // Thread.yield() 加不加不影响结果
        }
    }

    private fun unlock() {
        locked.compareAndSet(expect = true, update = false)
    }
}