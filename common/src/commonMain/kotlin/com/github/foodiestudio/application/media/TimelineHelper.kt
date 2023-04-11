package com.github.foodiestudio.application.media

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import com.github.foodiestudio.application.data.CaptionBlock
import java.time.Duration

/**
 * 一次性的计算出来用于计算文字轴和时间轴的映射关系
 */
object TimelineHelper {

    /**
     * 根据当前的 [layoutInfo] 提供的宽度信息，以及 [captions] 提供的时长信息，计算出 [duration] 在当前 viewport 所对应的位置
     * 确切的说，只需要知道 offset 和 size 就可以了，offset 值可能小于 0
     * 异常的情况，会返回 [BRollItemPosition.Unspecific]
     *
     * @param dataIndexForFirstLayoutVisibleItem 也就是 [LazyListLayoutInfo.visibleItemsInfo] 第一个元素所对应的 captions 里的 index
     */
    fun measure(
        captions: List<CaptionBlock>,
        dataIndexForFirstLayoutVisibleItem: Int,
        layoutInfo: LazyListLayoutInfo,
        duration: LongRange
    ): BRollItemPosition {
        if (layoutInfo.visibleItemsInfo.isEmpty()) {
            return BRollItemPosition.Unspecific
        }
        // 这个 duration 横跨多个 caption
        val startCaptionIndex = captions.indexOfLast { it.pts <= duration.first }
        val endCaptionIndex = captions.indexOfFirst { it.pts <= duration.last && duration.last < it.pts + it.duration }

        // 如果 [startCaptionIndex, endCaptionIndex] 不在可见的区间内，则不需要绘制
        // TODO 需要快速失败
        if (startCaptionIndex > dataIndexForFirstLayoutVisibleItem + layoutInfo.visibleItemsInfo.lastIndex || endCaptionIndex < dataIndexForFirstLayoutVisibleItem) {
            return BRollItemPosition.Unspecific
        }

        // 当前 viewport 可见的 caption
        val firstVisibleCaptionIndex = maxOf(dataIndexForFirstLayoutVisibleItem, startCaptionIndex)
        val lastVisibleCaptionIndex =
            minOf(dataIndexForFirstLayoutVisibleItem + layoutInfo.visibleItemsInfo.lastIndex, endCaptionIndex)

        assert(firstVisibleCaptionIndex <= lastVisibleCaptionIndex)

        val startVisibleItem =
            layoutInfo.visibleItemsInfo[firstVisibleCaptionIndex - dataIndexForFirstLayoutVisibleItem]
        val endVisibleItem = layoutInfo.visibleItemsInfo[lastVisibleCaptionIndex - dataIndexForFirstLayoutVisibleItem]

        // TODO 已知问题，在头部边界的时候，因为丢失了前面一些视图的长度信息，无法正确的估计出 B Roll 的长度，所以这里的长度会按大概的方式预估下
        val extraStartOffset = if (startCaptionIndex < dataIndexForFirstLayoutVisibleItem) {
            0
        } else {
            startVisibleItem.size * getItemFraction(
                duration.first,
                captions[startCaptionIndex]
            )
        }
        val extraEndOffset =
            if (dataIndexForFirstLayoutVisibleItem + layoutInfo.visibleItemsInfo.lastIndex < endCaptionIndex) {
                // 超出边界的话，不算
                endVisibleItem.size
            } else {
                endVisibleItem.size * getItemFraction(
                    duration.last,
                    captions[endCaptionIndex]
                )
            }

        val start =
            startVisibleItem.offset + extraStartOffset.toInt()
        val end = endVisibleItem.offset + extraEndOffset.toInt()

        return BRollItemPosition(
            start - layoutInfo.viewportStartOffset /*这个offset可以对齐到屏幕最左侧*/,
            end - start
        )
    }

    /**
     * 查询某个点对应的时间戳
     * 如果这个点不在可见的时间轴上的话，返回 -1
     */
    fun queryPts(
        captions: List<CaptionBlock>,
        dataIndexForFirstLayoutVisibleItem: Int,
        layoutInfo: LazyListLayoutInfo,
        position: Int
    ): Long {
        // 边界判断
        val relativeOffset = position + layoutInfo.viewportStartOffset
        if (relativeOffset < layoutInfo.visibleItemsInfo.first().offset || relativeOffset > layoutInfo.visibleItemsInfo.last()
                .let { it.offset + it.size }
        ) {
            return -1
        }
        val visibleIndex = layoutInfo.visibleItemsInfo.indexOfFirst { relativeOffset in it.offset..it.offset + it.size }
        val dataIndex = visibleIndex + dataIndexForFirstLayoutVisibleItem
        val fraction = getItemFraction(
            relativeOffset,
            layoutInfo.visibleItemsInfo[visibleIndex]
        )
        return (captions[dataIndex].duration * fraction).toLong() + captions[dataIndex].pts
    }

    private fun getItemFraction(offset: Int, item: LazyListItemInfo): Float {
        assert(offset in item.offset..item.offset + item.size)
        return (offset - item.offset).toFloat() / item.size
    }

    private fun getItemFraction(pts: Long, block: CaptionBlock): Float {
        assert(pts in block.pts until block.pts + block.duration)
        return (pts - block.pts).toFloat() / block.duration
    }
}

/**
 * firstVisibleItem 在 visibleItemsInfo 所对应的 index
 */
fun LazyListState.layoutIndexOfFirstVisibleItem(): Int {
    return layoutInfo.visibleItemsInfo.indexOfFirst { it.offset + firstVisibleItemScrollOffset == 0 }
}
