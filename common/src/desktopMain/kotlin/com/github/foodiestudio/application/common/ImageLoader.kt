package com.github.foodiestudio.application.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.seiko.imageloader.rememberAsyncImagePainter

@Composable
actual fun rememberImagePainter(url: String): Painter {
    return rememberAsyncImagePainter(url)
}

@Composable
actual fun AsyncImage(url: String, modifier: Modifier) {
    Image(
        painter = rememberImagePainter(url),
        contentDescription = null,
        modifier = modifier
    )
}