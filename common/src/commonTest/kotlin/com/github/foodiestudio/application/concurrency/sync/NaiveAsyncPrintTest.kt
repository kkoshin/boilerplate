package com.github.foodiestudio.application.concurrency.sync

import com.github.foodiestudio.application.concurrency.SpinLock
import kotlinx.atomicfu.atomic
import org.junit.Test
import kotlin.concurrent.thread

/**
 * 在多线程编程中，仅实现互斥的时候，并没有考虑执行的先后顺序，只是保证了执行块不被打断。
 * 这是一个很简单的例子，比较规则的执行一个顺序
 */
class NaiveAsyncPrintTest {

    private var step = atomic(0)

    private val lock = SpinLock()

    private var length = 10

    /**
     * 打印出 <>_<>_<>_
     */
    @Test
    fun `print pairs`() {
        val threads = listOf(
            thread(start = false) {
                for (i in 1..length) {
                    printLeft()
                }
            },
            thread(start = false) {
                for (i in 1..length) {
                    printRight()
                }
            },
            thread(start = false) {
                for (i in 1..length) {
                    printDash()
                }
            },
        )
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        println()
    }

    // stop the world
    private fun awaitStep(expect: Int) {
        while (step.value != expect) {
            // spin
        }
    }

    private fun printLeft() {
        awaitStep(0)
        lock.withSpinLock {
            print("<")
            System.out.flush() // print 有 buffer, 可能打印不出来，这样可以强制清空 buffer
            step.getAndSet(1)
        }
    }

    private fun printRight() {
        awaitStep(1)
        lock.withSpinLock {
            print(">")
            System.out.flush() // print 有 buffer, 可能打印不出来，这样可以强制清空 buffer
            step.getAndSet(2)
        }
    }

    private fun printDash() {
        awaitStep(2)
        lock.withSpinLock {
            print("_")
            System.out.flush() // print 有 buffer, 可能打印不出来，这样可以强制清空 buffer
            step.getAndSet(0)
        }
    }
}