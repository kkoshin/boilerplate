package com.github.foodiestudio.application

import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import com.github.foodiestudio.application.media.EditorScreen
import com.github.foodiestudio.application.media.VideoPlayer
import com.github.foodiestudio.application.FlipCard
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

// TODO 无法识别到 common 模块里的 preview，所以只能在这里额外添加，以此在 showkase 展示

@Preview(name = "VideoEditor", group = "Media components")
@Composable
fun VideoEditorPrev() = EditorScreen()

@Preview(name = "VideoPlayer", group = "Media components")
@Composable
fun VideoPlayerPrev() = VideoPlayer()

@Preview(name = "FlipCard", group = "Playground")
@Composable
fun FlipCardPrev() = FlipCard()

@OptIn(FlowPreview::class)
@Preview(name = "FlowTextButton", group = "Playground")
@Composable
fun ClickFlow() {
    val buttonState = rememberFlowButtonState("Haha")
    val haptic = LocalHapticFeedback.current

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        buttonState.clicks
            .debounce(200.milliseconds)
            .onEach {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                Toast.makeText(context, "clicked!", Toast.LENGTH_SHORT).show()
            }
            .collect()

// 如果不感兴趣的话，可以不要订阅
//            buttonState.selects
//                .flowOn(MainUIDispatcher)
//                .debounce(200.milliseconds)
//                .onEach {
//                    // 更新选中状态
//                    buttonState.text = "Selected + ${count++}"
//                }
//                .collect()
    }

    FlowTextButton(buttonState)
}