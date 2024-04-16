package com.github.foodiestudio.application.concurrency

import kotlinx.atomicfu.atomic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * 多线程并发计数器，需要互斥来保证结果的正确。
 */
class ConcurrentCountTest {

    // 此用例情况比较简单，为了避免被编译器优化掉，这里可以使用 @Volatile
    @Volatile
    private var count = 0

    private val atomicCount = AtomicInteger(0)

    private val atomicCountKt = atomic(0)

    // 在比较小的循环的话，比较难出现问题，所以需要设置大一点
    private val stepCount = 10000

    @Before
    @Test
    fun setup() {
        count = 0
        atomicCount.set(0)
        atomicCountKt.value = 0
    }

    /**
     * 不做任何处理，直接并发执行, 会出现错误的结果
     * 多个线程切换，导致读到一些中间结果，读写不一致。
     */
    @Test
    fun `invalid count`() {
        val threads = List(4) {
            thread(start = false) {
                // 在比较小的循环的话，比较难出现问题，所以需要设置大一点
                for (i in 1..stepCount) {
                    count += i  // Concurrent access without synchronization
                }
            }
        }
        measureTimeMillis {
            threads.forEach { it.start() }
            threads.forEach { it.join() } // Wait for threads to finish
        }.let {
            println("invalid count result: $count in $it ms") // 期望每次的结果都有所不同
        }
        assertEquals(false, count == (1 + stepCount) * stepCount / 2 * 4)
    }

    /**
     * 解决原子化的问题，以加法为例
     */
    @Test
    fun `concurrent count with atomic in Java Style`() {
        val threads = List(4) {
            thread(start = false) {
                for (i in 1..stepCount) {
                    atomicCount.getAndAdd(i) // Concurrent access without synchronization
                }
            }
        }
        measureTimeMillis {
            threads.forEach { it.start() }
            threads.forEach { it.join() } // Wait for threads to finish
        }.let {
            println("atomic count result(Java): $atomicCount in $it ms")
        }
        assertEquals(true, atomicCount.get() == (1 + stepCount) * stepCount / 2 * 4)
    }

    /**
     * 采用原子化指令，可以处理比较简单的运算。
     * 运行效率也比较高。
     */
    @Test
    fun `concurrent count with atomic in Kotlin Style`() {
        val threads = List(4) {
            thread(start = false) {
                for (i in 1..stepCount) {
                    // addAndGet 或者 getAndAdd 在这个例子里结果没有什么区别
                    atomicCountKt.getAndAdd(i)
                }
            }
        }
        measureTimeMillis {
            threads.forEach { it.start() }
            threads.forEach { it.join() } // Wait for threads to finish
        }.let {
            println("atomic count result(Kt): $atomicCountKt in $it ms")
        }
        assertEquals(true, atomicCountKt.value == (1 + stepCount) * stepCount / 2 * 4)
    }

    /**
     * 自旋（空转）保证了线程安全，但浪费了不少计算资源
     */
    @Test
    fun `concurrent count with spinLock`() {
        val spinLock = SpinLock()
        val threads = List(4) {
            thread(start = false) {
                for (i in 1..stepCount) {
                    spinLock.withSpinLock {
                        count += i
                    }
                }
            }
        }
        measureTimeMillis {
            threads.forEach { it.start() }
            threads.forEach { it.join() } // Wait for threads to finish
        }.let {
            println("valid count result(Spin lock): $count in $it ms")
        }
        assertEquals(true, count == (1 + stepCount) * stepCount / 2 * 4)
    }
}