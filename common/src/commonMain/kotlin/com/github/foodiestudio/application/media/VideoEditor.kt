package com.github.foodiestudio.application.media

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.data.FakeData
import com.github.foodiestudio.application.data.TrackData
import com.github.foodiestudio.application.data.calculatePts
import com.github.foodiestudio.application.common.logcat
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

// 可能有点违背单一数据原则（ptsMills）触发页面滚动，用户自身也能手动触发滚动，为了让滚动更流畅，会内部先响应滚动，然后再同步给外面的播放器
@Composable
fun VideoEditor(
    state: VideoEditorState = rememberVideoEditorState(),
) {
    val density = LocalDensity.current
    val captions = FakeData.captions

    var timelinePts by remember {
        mutableStateOf(state.ptsMills)
    }

    val lazyScrollState = rememberLazyListState(0, 0)

    // 主要是为了区分内部触发
    var targetItemAndOffset by remember {
        mutableStateOf((0 to 0))
    }

    // 这种方式的改动是直接修改，是无法观测到滚动过程中的 index/offset 的值的变化，最终在滚动完成后，可以查询到 firstVisibleItemIndex 的变化
    LaunchedEffect(lazyScrollState, state.ptsMills) {
        if (state.ptsMills == timelinePts) {
            // ignore ptsMills changed
            return@LaunchedEffect
        }
        // FIXME captions 是排序过的，这里可以用二分查找的方式查询提高效率
        captions.indexOfLast {
            it.pts <= state.ptsMills
        }.let {
            timelinePts = state.ptsMills
            // 第一次滚动的时候，无法查询对应的元素的宽度，只能让他变为可见
            lazyScrollState.scrollToItem(it)
            // 第一次滚动完成后，因为可见，所以可以查询到对应的元素的宽度
            val fraction = (state.ptsMills - captions[it].pts).toFloat() / captions[it].duration
            // 当前 firstVisibleItemScrollOffset 为 0，对应的元素也就是 firstVisibleItemIndex 指向的
            // FIXME 通过 key 来查找 item
            val item = lazyScrollState.layoutInfo.visibleItemsInfo.first { it.offset == 0 }
            val offset = item.size * (fraction)
            targetItemAndOffset = it to offset.toInt()
            lazyScrollState.scrollToItem(it, offset.toInt())
        }
    }

    // 貌似 lazyScrollState.scrollToItem(it) 处理了在滚动中的情况下请求滚动的情况，cancel 掉之前的？
    // 在当前这个例子里，就是屏幕居中的元素
    val firstVisibleItemIndex by remember {
        derivedStateOf {
            lazyScrollState.firstVisibleItemIndex
        }
    }
    // 这个值始终 >= 0
    val firstVisibleItemScrollOffset by remember {
        derivedStateOf {
            lazyScrollState.firstVisibleItemScrollOffset
        }
    }
    LaunchedEffect(firstVisibleItemIndex, firstVisibleItemScrollOffset) {
        if (firstVisibleItemIndex == targetItemAndOffset.first && firstVisibleItemScrollOffset == targetItemAndOffset.second) {
            return@LaunchedEffect
        }
        // FIXME 用 key 的方式查找 item 看上去可能更直接
        val itemIndex = lazyScrollState.layoutIndexOfFirstVisibleItem()
        val item = lazyScrollState.layoutInfo.visibleItemsInfo[itemIndex]
        val pts = captions[firstVisibleItemIndex].calculatePts(firstVisibleItemScrollOffset.toFloat() / item.size)
        // 这里的滚动是内部触发的，内部自己刷新了 pts，onSeekRequest 会将这个 pts 变更同步到上层
        if (timelinePts != pts) {
            logcat {
                """
                    timelinePts: $timelinePts
                    pts: $pts
                """.trimIndent()
            }
            timelinePts = pts
            state.seekTo(pts)
        }
    }

    val scope = rememberCoroutineScope()

    MaterialTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(rememberScrollableState {
                    // View component deltas should be reflected in Compose
                    // components that participate in nested scrolling
                    scope.launch {
                        lazyScrollState.scrollBy(-it)
                    }
                    it
                }, Orientation.Horizontal)
                .background(Color.Black),
        ) {
            val constraints = constraints
            Column(Modifier.padding(vertical = 16.dp)) {
                BRollGroup(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 36.dp * state.bRolls.size.coerceAtMost(3), max = 120.dp)
                        .padding(vertical = 0.dp),
                    rolls = state.bRolls,
                    getVisibleTimeLineRange = {
                        // TODO(Jiangc): 返回可见的区间
                        0..1
                    },
                    timelineMeasure = {
                        TimelineHelper.measure(
                            captions,
                            lazyScrollState.firstVisibleItemIndex - lazyScrollState.layoutIndexOfFirstVisibleItem(),
                            lazyScrollState.layoutInfo,
                            it
                        )
                    }
                )
                ARoll(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    state = lazyScrollState,
                    captions = captions,
                    contentPaddingValues = PaddingValues(
                        horizontal = with(density) {
                            constraints.maxWidth.toDp() / 2
                        }
                    ),
                    onClick = {
                        state.seekTo(it)
                    }
                )
            }

            Box(
                Modifier.matchParentSize(), contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(Color.White, RoundedCornerShape(50))
                        .fillMaxHeight()
                        .width(2.dp)
                )
            }
        }
    }
}

// fun List<CaptionBlock>.findNearestIndexOfLast(ptsMills: Int): Int {
//    if (this.isEmpty()) return -1
//    if (ptsMills < this.first().pts)
//    return binarySearch {
//        when {
//            ptsMills < it.pts -> 1
//            it.pts + it.duration < ptsMills -> 0
//            else -> -1
//        }
//    }
// }

@Composable
fun rememberVideoEditorState(
    ptsMills: Long = 0,
    bRolls: TrackData = emptyList(),
): VideoEditorState = remember {
    VideoEditorStateImpl(ptsMills, bRolls)
}

interface VideoEditorState {
    var ptsMills: Long

    var bRolls: TrackData

    val seekRequests: Flow<Long>

    fun seekTo(ptsMills: Long)
}

class VideoEditorStateImpl(
    ptsMills: Long,
    bRolls: TrackData
) : VideoEditorState {
    override var ptsMills: Long by mutableStateOf(ptsMills)
    override var bRolls: TrackData by mutableStateOf(bRolls)
    private val _seekRequests =
        MutableSharedFlow<Long>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val seekRequests: Flow<Long> = _seekRequests

    override fun seekTo(ptsMills: Long) {
        _seekRequests.tryEmit(ptsMills)
    }
}
