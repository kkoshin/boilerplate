package com.github.foodiestudio.application.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.rememberAsyncImagePainter

@Composable
actual fun rememberImagePainter(url: String): Painter {
    return rememberAsyncImagePainter(url)
}

@Composable
actual fun AsyncImage(url: String, modifier: Modifier) {
    coil.compose.AsyncImage(
        modifier = modifier,
        model = url,
        contentDescription = null,
    )
}