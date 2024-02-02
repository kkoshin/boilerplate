package com.github.foodiestudio.application.storage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.foodiestudio.application.theme.ApplicationTheme

class StorageManageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApplicationTheme {
                ScopedStoragePlayground(Modifier.padding(16.dp))
            }
        }
    }
}