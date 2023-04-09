package com.github.foodiestudio.application.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.data.CaptionBlock

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ARoll(
    modifier: Modifier,
    state: LazyListState,
    captions: List<CaptionBlock>,
    contentPaddingValues: PaddingValues,
    onClick: (Int) -> Unit
) {
    var msg: String? by remember {
        mutableStateOf(null)
    }
    LazyRow(state = state, contentPadding = contentPaddingValues, userScrollEnabled = true, modifier = modifier) {
        items(captions, key = { it.pts }) {
            CaptionItem(phrase = it) {
//                msg = it.text
                onClick(it.pts)
            }
        }
    }

    msg?.let {
        AlertDialog(
            onDismissRequest = { msg = null },
            modifier = Modifier.widthIn(min = 300.dp),
            text = { Text(it) },
            buttons = {
                TextButton(onClick = { msg = null }, modifier = Modifier.fillMaxWidth()) {
                    Text("Confirm")
                }
            }
        )
    }
}

@Composable
fun CaptionItem(phrase: CaptionBlock, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = "${phrase.pts}/${phrase.text}")
    }
}
