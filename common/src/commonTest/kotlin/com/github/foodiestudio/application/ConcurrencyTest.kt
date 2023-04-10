package com.github.foodiestudio.application

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ConcurrencyTest {

    @Test
    fun count() {
        val executor = Executors.newFixedThreadPool(20)
        var count = 0
        repeat(100) {
            executor.execute {
                count++
            }
        }
        executor.awaitTermination(30, TimeUnit.SECONDS)
        assertEquals(100, count)
    }
}