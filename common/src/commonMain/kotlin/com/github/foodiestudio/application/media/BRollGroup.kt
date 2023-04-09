package com.github.foodiestudio.application.media

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.data.TrackData

// 当文字一定的情况下，这些 B-Roll 一旦计算过，这个映射关系可以缓存下来，下次直接复用
// TODO 当前的列表的滚动位置 -> 时间轴和画面的映射曲线（多段折线组成） -> 一条 B-Roll 对应的元素依次根据其时间区间查询得到对应的 Px 区间 -> 然后只需要绘制在屏幕区域内的
@Composable
fun BRollGroup(
    modifier: Modifier,
    rolls: TrackData,
    getVisibleTimeLineRange: () -> IntRange,
    timelineMeasure: (IntRange) -> Pair<Int, Size>
) {
    val density = LocalDensity.current
    val visibleTimeLineRange: IntRange by remember(rolls) {
        mutableStateOf(getVisibleTimeLineRange())
    }

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // 先过滤一波不可见的 roll
//        rolls.trackData.filter { roll ->
//            roll.any { it.duration - visibleTimeLineRange}
//        }
        items(rolls.trackData) {
            // 查询每个 roll 里的元素位置信息
            val (offset, size) = timelineMeasure(it.first().duration)
            if (size.isUnspecified) {
                Spacer(Modifier)
            } else {
                with(density) {
                    EffectItem(
                        modifier = Modifier.offset(x = offset.toDp()),
                        DpSize(width = size.width.toDp(), 32.dp)
                    ) {
                    }
                }
            }
        }
    }
}

@Composable
fun EffectItem(
    modifier: Modifier,
    contentSize: DpSize,
    onClick: () -> Unit
) {
    var selected: Boolean by remember {
        mutableStateOf(false)
    }

    if (selected) {
        Row(
            modifier = modifier
                .background(Color.White, RoundedCornerShape(4.dp))
                .padding(vertical = 4.dp)
                .width(contentSize.width + indicatorWidthDp * 2)
                .height(IntrinsicSize.Min)
        ) {
            Indicator(modifier = Modifier.fillMaxHeight())
            Text(
                "XXXX",
                Modifier
                    .background(Color.Cyan)
                    .weight(1f)
            )
            Indicator(modifier = Modifier.fillMaxHeight())
        }
    } else {
        Box(
            modifier = modifier
                .background(Color.Cyan, RoundedCornerShape(4.dp))
                .size(contentSize)
                .clickable { selected = !selected }
        ) {
            Text("Sample")
        }
    }
}

val indicatorWidthDp = 12.dp

@Composable
fun Indicator(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier.width(12.dp)
    ) {
        Spacer(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(50))
                .width(2.dp)
                .height(10.dp)
        )
    }
}