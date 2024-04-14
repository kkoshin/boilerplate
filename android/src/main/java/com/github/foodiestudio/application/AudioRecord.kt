package com.github.foodiestudio.application

import android.media.MediaRecorder
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.foodiestudio.sugar.ExperimentalSugarApi
import com.github.foodiestudio.sugar.storage.AppFileHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Preview(name = "record", group = "Playground")
@Composable
fun AudioRecordSample(modifier: Modifier = Modifier) {
    val recordPermissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    if (recordPermissionState.status.isGranted) {
        RecordButton()
    } else {
        Column {
            val textToShow = if (recordPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Record permission required for this feature to be available. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { recordPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}

@OptIn(ExperimentalSugarApi::class)
@Composable
fun RecordButton(modifier: Modifier = Modifier) {
    var isRecording by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val outputFile = remember {
        val dir = AppFileHelper(context.applicationContext).requireFilesDir(false)
        File(dir, "testRecordAudio.3gp").apply {
            createNewFile()
        }
    }
    val mediaRecorder = remember {
        MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(outputFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            prepare()
        }
//        更精细的控制需使用 AudioRecord 方式实现
//        AudioRecord(
//            MediaRecorder.AudioSource.MIC,
//            44100, // 采样率为 44.1 kHz
//            AudioFormat.CHANNEL_IN_MONO,
//            AudioFormat.ENCODING_PCM_16BIT,
//            16
//        )
    }

    DisposableEffect(mediaRecorder) {
        onDispose {
            mediaRecorder.release()
        }
    }

    if (isRecording) {
        IconButton(onClick = {
            // stop
            mediaRecorder.stop()
            isRecording = false
        }) {
            Icon(Icons.Default.Stop, null)
        }
    } else {
        IconButton(onClick = {
            // start
            mediaRecorder.start()
            isRecording = true
        }) {
            Icon(Icons.Default.PlayArrow, null)
        }
    }
}