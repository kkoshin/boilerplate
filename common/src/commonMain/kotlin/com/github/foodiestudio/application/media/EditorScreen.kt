package com.github.foodiestudio.application.media

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.data.EffectData
import com.github.foodiestudio.application.data.FakeData
import com.github.foodiestudio.application.data.Track
import com.github.foodiestudio.application.data.TrackType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorScreen() {
    val editorState = rememberVideoEditorState(0L, FakeData.trackGroupData)

    LaunchedEffect(Unit) {
        editorState.seekRequests
            .onEach {
                editorState.ptsMills = it
            }
            .collect()
    }

    Column {
        PlaybackBar(editorState.ptsMills.toFloat(), onValueChange = {
            editorState.ptsMills = it.toLong()
        })
        VideoEditor(editorState)
        MenuBar {
            if (it == Menu.StyledText) {
                editorState.bRolls = editorState.bRolls.toMutableList().apply {
                    add(
                        Track(
                            track = listOf(EffectData.StyledText(editorState.ptsMills..(editorState.ptsMills + 1000L))),
                            trackType = TrackType.StyledText
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PlaybackBar(value: Float, onValueChange: (Float) -> Unit) {
    Column(Modifier.background(Color.Black)) {
        Slider(value, valueRange = 0f..1000f, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth())
        Text(
            "${value.toInt()}/1000",
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
            color = Color.LightGray
        )
    }
}

enum class Menu {
    StyledText, Sticker, Media, Gif
}

@Composable
fun MenuBar(onMenuClick: (Menu) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        Modifier.fillMaxWidth().horizontalScroll(scrollState)
            .height(80.dp)
            .background(Color.Black)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Menu.values().forEach {
            Button(onClick = { onMenuClick(it) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Add, null, tint = Color.LightGray)
                    Text(it.name)
                }
            }
        }
    }
}