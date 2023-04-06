package com.github.foodiestudio.application.media

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.data.FakeData
import com.github.foodiestudio.application.data.TrackData

@Preview(name = "VideoEditor", group = "Media components")
@Composable
fun VideoEditor() {
    val density = LocalDensity.current
    MaterialTheme {
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .background(Color.DarkGray),
        ) {
            val constraints = constraints
            Column(Modifier.padding(vertical = 16.dp)) {
                BRollGroup(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 120.dp)
                        .padding(vertical = 4.dp),
                    FakeData.trackData
                )
                ARoll(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    captions = FakeData.captions,
                    contentPaddingValues = PaddingValues(horizontal = with(density) {
                        constraints.maxWidth.toDp() / 2
                    })
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

@Composable
fun ARoll(modifier: Modifier, captions: List<String>, contentPaddingValues: PaddingValues) {
    LazyRow(contentPadding = contentPaddingValues, userScrollEnabled = true, modifier = modifier) {
        items(captions) {
            CaptionItem(phrase = it) {

            }
        }
    }
}

@Composable
fun CaptionItem(phrase: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = phrase)
    }
}

@Composable
fun BRollGroup(modifier: Modifier, rolls: TrackData) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(rolls.trackData) {
            EffectItem(modifier = Modifier.offset(x = 100.dp), DpSize(width = 108.dp, 32.dp), {

            })
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
                .clickable { selected = !selected }) {
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

