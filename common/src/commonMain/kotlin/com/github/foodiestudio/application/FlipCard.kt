package com.github.foodiestudio.application

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.common.AsyncImage
import com.github.foodiestudio.application.common.rememberImagePainter
import kotlin.math.abs

@Composable
fun FlipCard() {
    var rotationX by remember {
        mutableStateOf(0f)
    }
    var rotationY by remember {
        mutableStateOf(0f)
    }
    var rotationZ by remember {
        mutableStateOf(0f)
    }
    Column(Modifier.padding(16.dp)) {
        Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
            DoubleSide(
                rotationX = rotationX,
                rotationY = rotationY,
                rotationZ = rotationZ,
                front = {
                    AsyncImage(
                        "https://www.gstatic.com/android/keyboard/emojikitchen/20201001/u1f600/u1f600_u1f642.png",
                        Modifier.size(120.dp, 120.dp),
                    )
                },
                back = {
                    AsyncImage(
                        "https://www.gstatic.com/android/keyboard/emojikitchen/20201001/u1f62d/u1f62d_u1f642.png",
                        Modifier.size(120.dp, 120.dp),
                    )
                },
            )
        }
        SlideBar("Horizontal Flip(X Axis)", rotationX, 0.0F..360.0F) {
            rotationX = it
        }
        SlideBar("Horizontal Flip(Y Axis)", rotationY, 0.0F..360.0F) {
            rotationY = it
        }
        SlideBar("Rotation(Z Axis)", rotationZ, 0.0F..360.0F) {
            rotationZ = it
        }
    }
}

@Composable
private fun SlideBar(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit,
) {
    Column {
        Row {
            Text(label, modifier = Modifier.weight(1f))
            Text(value.toInt().toString())
        }
        Slider(value, valueRange = valueRange, onValueChange = onValueChanged)
    }
}

@Composable
private fun DoubleSide(
    translationX: Float = 0f,
    translationY: Float = 0f,
    rotationX: Float = 0f,
    rotationY: Float = 0f,
    rotationZ: Float = 0f,
    cameraDistance: Float = 8f,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
) {
    val isHorizontalFlip: Boolean =
        (abs(rotationX) % 360).let {
            it > 90F && it < 270F
        }
    val isVerticalFlip: Boolean =
        (abs(rotationY) % 360).let {
            it > 90F && it < 270F
        }
    if (isHorizontalFlip xor isVerticalFlip) {
        Box(
            Modifier.graphicsLayer(
                translationX = translationX,
                translationY = translationY,
                rotationX = rotationX,
                rotationY = rotationY,
                rotationZ = -rotationZ,
                cameraDistance = cameraDistance,
            ),
        ) {
            back()
        }
    } else {
        Box(
            Modifier
                .graphicsLayer(
                    translationX = translationX,
                    translationY = translationY,
                    rotationX = rotationX,
                    rotationY = rotationY,
                    rotationZ = rotationZ,
                    cameraDistance = cameraDistance,
                ),
        ) {
            front()
        }
    }
}
