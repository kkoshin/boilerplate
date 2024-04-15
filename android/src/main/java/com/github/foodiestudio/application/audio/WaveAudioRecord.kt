package com.github.foodiestudio.application.audio

import android.media.AudioRecord
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.foodiestudio.sugar.ExperimentalSugarApi
import com.github.foodiestudio.sugar.notification.toast
import com.github.foodiestudio.sugar.storage.AppFileHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Preview(name = "record", group = "Playground")
@Composable
fun WaveRecord(modifier: Modifier = Modifier) {
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

private val waveConfig = WaveConfig()

@OptIn(ExperimentalSugarApi::class)
@Composable
fun RecordButton(modifier: Modifier = Modifier) {
    var isRecording by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val wavFile = remember {
        val dir = AppFileHelper(context.applicationContext).requireFilesDir(false)
        File(dir, "testRecord.wav")
    }
    val mediaRecorder = remember {
        AudioRecord(
            MediaRecorder.AudioSource.MIC,
            waveConfig.sampleRate,
            waveConfig.channels,
            waveConfig.audioEncoding,
            waveConfig.recordBufferSize
        )
    }

    val scope = rememberCoroutineScope()

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
            mediaRecorder.startRecording()
            isRecording = true
            scope.launch(Dispatchers.IO) {
                val buffer = ByteArray(waveConfig.recordBufferSize)
                wavFile.sink().buffer().use { sink ->
                    while (isRecording) {
                        val read = mediaRecorder.read(buffer, 0, waveConfig.recordBufferSize)
                        if (read > 0) {
                            sink.write(buffer, 0, read)
                        }
                    }
                    sink.flush()
                }
                runCatching {
                    WaveHeaderWriter(
                        filePath = wavFile.absolutePath,
                        waveConfig = WaveConfig()
                    ).writeHeader()
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        context.toast("save successful.")
                    }
                }
//                runCatching {
//                    AudioUtil.pcmToWav(
//                        pcmFile,
//                        wavFile,
//                        sampleRate,
//                        16,
//                        channelConfig == AudioFormat.CHANNEL_IN_STEREO
//                    )
//                }.onSuccess {
//                    withContext(Dispatchers.Main) {
//                        context.toast("save successful.")
//                    }
//                }
            }
        }) {
            Icon(Icons.Default.PlayArrow, null)
        }
    }
}