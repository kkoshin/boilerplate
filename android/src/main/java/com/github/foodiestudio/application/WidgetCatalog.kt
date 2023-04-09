package com.github.foodiestudio.application

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.foodiestudio.application.media.EditorScreen
import com.github.foodiestudio.application.media.VideoPlayer

// TODO 无法识别到 common 模块里的 preview，所以只能在这里额外添加，以此在 showkase 展示

@Preview(name = "VideoEditor", group = "Media components")
@Composable
fun VideoEditorPrev() = EditorScreen()

@Preview(name = "VideoPlayer", group = "Media components")
@Composable
fun VideoPlayerPrev() = VideoPlayer()