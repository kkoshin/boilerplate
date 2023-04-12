import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.logcat

/**
 * 验证 LazyList 的联动，Slider 的进度和 下面的 List 包括同步
 *
 * 并没有找到一个比较好的方式区分用户拖动和 Slider 触发的 List 滚动，只能用 Scroll 完成后的结果倒推。
 */
@Composable
fun SliderAndList() {
    var progress by remember {
        mutableStateOf(0f)
    }
    val data = List(100) { it }
    val lazyListState = rememberLazyListState()

    var innerProgress by remember {
        mutableStateOf(0F)
    }

    LaunchedEffect(progress) {
        // 1
        if (progress == innerProgress) {
            return@LaunchedEffect
        }
        innerProgress = progress
        lazyListState.scrollToItem(progress.toIndex())
    }

    val firstVisibleItemIndex by remember {
        derivedStateOf {
            // 2
            lazyListState.firstVisibleItemIndex
        }
    }

    LaunchedEffect(firstVisibleItemIndex) {
        // 3
        if (innerProgress.toIndex() != firstVisibleItemIndex) {
            logcat {
                "seekRequest"
            }
            innerProgress = firstVisibleItemIndex.toProgress()
            progress = innerProgress
        }
    }


    Column {
        Slider(value = progress, onValueChange = {
            progress = it
        })
        LazyRow(state = lazyListState) {
            items(data) {
                Text("$it", Modifier.padding(16.dp))
            }
        }
    }
}

// 1.1234 -> 112
private fun Float.toIndex(): Int = (this * 100).toInt()

// 112 -> 1.12
private fun Int.toProgress(): Float = this.toFloat() / 100