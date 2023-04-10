package com.github.foodiestudio.application.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.foodiestudio.application.data.CaptionBlock

@Composable
fun ARoll(
    modifier: Modifier,
    state: LazyListState,
    captions: List<CaptionBlock>,
    contentPaddingValues: PaddingValues,
    onClick: (Int) -> Unit
) {
    LazyRow(
        state = state,
        contentPadding = contentPaddingValues,
        userScrollEnabled = false,
        modifier = modifier
    ) {
        items(captions, key = { it.pts }) {
            CaptionItem(phrase = it) {
                onClick(it.pts)
            }
        }
    }
}

@Composable
fun CaptionItem(phrase: CaptionBlock, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = "${phrase.pts}/${phrase.text}")
    }
}
