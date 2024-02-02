package com.github.foodiestudio.application.storage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toast
import java.io.File

/**
 * - Android 11：对别的 app 的 cache 文件的访问做了限制，如果要查询剩余空间以及清除 cache.
 * 需要使用 `ACTION_MANAGE_STORAGE`(这个上架的话需要提供额外说明) 和 `ACTION_CLEAR_APP_CACHE`
 * - Android 10: 开始支持 Scoped Storage
 */
@Preview(name = "ScopedStorage", group = "Playground")
@Composable
fun ScopedStoragePlayground(modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        ManageAllFileButton()
        CacheQuota()
        WriteButton()
        ReadButton()
        TempFileButton()
        CreatePictureOnAppExternal()

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun CreatePictureOnAppExternal(modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    Button(onClick = {
        getAppSpecificAlbumStorageDir(appContext, "test")
        appContext.toast("success")
    }) {
        Text("在应用的外部存储创建 Picture 文件夹")
    }
}

// 其实没有一定说要按照这个目录名称去放文件
fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
    // Get the pictures directory that's inside the app-specific directory on
    // external storage.
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
    if (!file.mkdirs()) {
        context.toast("Directory not created")
    }
    return file
}

@Composable
fun TempFileButton(modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val appFileHelper = remember {
        AppFileHelper(appContext)
    }
    Button(onClick = {
        appFileHelper.createTempFile().outputStream().use {
            it.write("Test".toByteArray())
        }
        appContext.toast("success")
    }) {
        Text("Create temp file")
    }
}

@Composable
fun CacheQuota(modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val appFileHelper = remember {
        AppFileHelper(appContext)
    }
    var quota by remember {
        mutableStateOf(-1L)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            appFileHelper.getCacheQuotaBytes(true).onSuccess {
                quota = it / (1024 * 1024L)
            }
        }
    }

    Text("External App Cache Quota: $quota MB")
}

@Composable
fun WriteButton(modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val appFileHelper = remember {
        AppFileHelper(appContext)
    }
    Button(onClick = {
        appFileHelper.writeSensitiveFile("Foo.text") {
            it.write("Hello".toByteArray())
        }
        appContext.toast("success")
    }) {
        Text("Write")
    }
}

@Composable
fun ReadButton(modifier: Modifier = Modifier) {
    val appContext = LocalContext.current.applicationContext
    val appFileHelper = remember {
        AppFileHelper(appContext)
    }
    Button(onClick = {
        appContext.toast(appFileHelper.readSensitiveFile("Foo.text"))
    }) {
        Text("Read")
    }
}

private class ManageAllFilesAccess : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }
}

// https://developer.android.com/training/data-storage/manage-all-files
@Composable
private fun ManageAllFileButton(modifier: Modifier = Modifier) {
    var isHasStoragePermission by remember {
        mutableStateOf(Environment.isExternalStorageManager())
    }
    val launcher = rememberLauncherForActivityResult(ManageAllFilesAccess()) {
        isHasStoragePermission = Environment.isExternalStorageManager()
    }
    Button(
        enabled = !isHasStoragePermission,
        onClick = {
            launcher.launch(Unit)
        }) {
        Text(if (isHasStoragePermission) "All File Manage Permission Granted" else "Request All File Manage Permission")
    }
}