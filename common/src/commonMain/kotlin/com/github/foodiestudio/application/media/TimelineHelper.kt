package com.github.foodiestudio.application.media

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Size
import com.github.foodiestudio.application.data.CaptionBlock

/**
 * 一次性的计算出来用于计算文字轴和时间轴的映射关系
 */
object TimelineHelper {

    // 时间段 -> 长度段(全局的,不考虑 padding 的情况下)
    val lengthRangeMeasureResult: MutableMap<IntRange, IntRange> = mutableMapOf()

//    /**
//     * 根据 [timeDuration] 查询对应的绘制的长度
//     * 若查询不到的话，返回 null
//     */
//    fun queryLengthRange(timeDuration: IntRange): IntRange? {
//        check(!timeDuration.isEmpty())
//
//        return lengthRangeMeasureResult.getOrPut(timeDuration, {
//            1..2
//        })
//    }

    /**
     * 根据当前的 [layoutInfo] 提供的宽度信息，以及 [captions] 提供的时长信息，计算出 [duration] 在当前 viewport 所对应的位置
     * 确切的说，只需要知道 offset 和 size 就可以了，offset 值可能小于 0
     * 异常的情况，会返回 (0,Size.Unspecified）
     */
    fun measure(
        captions: List<CaptionBlock>,
        centerVisibleItemIndex: Int,
        layoutInfo: LazyListLayoutInfo,
        duration: IntRange
    ): Pair<Int, Size> {
        if (layoutInfo.visibleItemsInfo.isEmpty()) {
            return 0 to Size.Unspecified
        }
        // 这个 duration 横跨多个 caption
        val startCaptionIndex = captions.indexOfLast { it.pts <= duration.first }
        val endCaptionIndex = captions.indexOfLast { it.pts + it.duration < duration.last }

        val layoutIndexOffset = centerVisibleItemIndex
        // 当前 viewport 可见的 caption
        val firstVisibleCaptionIndex = layoutIndexOffset
        val lastVisibleCaptionIndex = layoutInfo.visibleItemsInfo.lastIndex + layoutIndexOffset

        // 绘制的情况总共有这几种
        // 1. duration's first edge is offset screen, when clicked, should not see first indicator, so need return at least [indicator width]
        //   1.0 duration's last edge is left offset screen, It's totally invisible, so just return (0 to Size.Unspecified)
        //   1.1 duration's last edge is small: It's just fine to return (offset, size)
        //   1.2 .1 duration length is too long, it's end edge is offset from screen. when it clicked, should not see last indicator, so ensure (offset + width) >= viewport size + indicator width
        // 2. duration's first edge is in screen
        //   2.0 duration length is small: It's just fine to return (offset, size)
        //   2.1 duration length is too long, it's end edge is offset from screen. when it clicked, should not see last indicator, so ensure (offset + width) >= viewport size + indicator width
        return layoutInfo.visibleItemsInfo.first().offset - layoutInfo.viewportStartOffset to Size(200F, 100F)
    }
}

/**
 * firstVisibleItem 在 visibleItemsInfo 所对应的 index
 */
fun LazyListState.layoutIndexOfFirstVisibleItem(): Int {
    return layoutInfo.visibleItemsInfo.indexOfFirst { it.offset + firstVisibleItemScrollOffset == 0 }
}
