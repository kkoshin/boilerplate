package com.github.foodiestudio.application.media

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.ui.unit.IntSize
import com.github.foodiestudio.application.data.CaptionBlock
import org.junit.Test

import org.junit.Assert.*

class TimelineHelperTest {

    private val bRollDuration = LongRange(20, 120)

    private val mockCaptions = listOf(
        CaptionBlock("0/in", 0, 20),
        CaptionBlock("20/video", 20, 50),
        CaptionBlock("70/production,", 70, 100),
        CaptionBlock("170/A-roll", 170, 50),
        CaptionBlock("220/is", 220, 20),
    )

    /**
     * 初始的状态下，也就是滚动到第 0 个位置上
     */
    @Test
    fun `scroll to first item without offset`() {
        var offset = 0
        val visibleItems = mockCaptions.take(4).mapIndexed { index, caption ->
            object : LazyListItemInfo {
                override val index: Int = index
                override val key: Any = Any()
                override val offset: Int = offset
                override val size: Int = 100 // hardcode
                //                override val size: Int = caption.text.length * 10 + 32
            }.also {
                offset += it.size
            }
        }
        assertEquals(true, visibleItems[2].offset > 0)
        val layoutInfo = createLayoutInfo(visibleItems)
        TimelineHelper.measure(mockCaptions, 0, layoutInfo, bRollDuration).let {
            assertEquals(100 - (-600), it.offset)
            assertEquals(150, it.width)
        }
    }

    /**
     * 初始的状态下，也就是滚动到第2个位置上, 并且带上额外的 10 px 偏移
     */
    @Test
    fun `scroll to second item with 10 offset`() {

    }

    @Test
    fun `query pts`() {
        var offset = 0
        val visibleItems = mockCaptions.take(4).mapIndexed { index, caption ->
            object : LazyListItemInfo {
                override val index: Int = index
                override val key: Any = Any()
                override val offset: Int = offset
                override val size: Int = 100 // hardcode
                //                override val size: Int = caption.text.length * 10 + 32
            }.also {
                offset += it.size
            }
        }
        assertEquals(true, visibleItems[2].offset > 0)
        val layoutInfo = createLayoutInfo(visibleItems)
        TimelineHelper.queryPts(mockCaptions, 0, layoutInfo, 100 - (-600) + 150).let {
            assertEquals(bRollDuration.last, it)
        }
    }

    private fun createLayoutInfo(visibleItems: List<LazyListItemInfo>): LazyListLayoutInfo {
        return object : LazyListLayoutInfo {
            override val totalItemsCount: Int
                get() = mockCaptions.size
            override val orientation: Orientation
                get() = Orientation.Horizontal
            override val viewportSize: IntSize
                get() = IntSize(1200, 100)
            override val viewportEndOffset: Int
                get() = 600
            override val viewportStartOffset: Int
                get() = -600
            override val visibleItemsInfo: List<LazyListItemInfo>
                get() = visibleItems
        }
    }
}
