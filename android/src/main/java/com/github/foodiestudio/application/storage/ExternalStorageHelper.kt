package com.github.foodiestudio.application.storage

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import java.io.InputStream

/**
 * 这里只讨论不需要权限请求的前提下。
 * 存储在应用外的共享文件夹中，独立于应用的生命周期，即便应用卸载了，这些文件也不会被删除
 *
 * - 不涉及 Cache
 * - 针对媒体文件需要使用 MediaStore 来处理，返回的是 MediaStore Uri
 * - 非媒体文件需要采用传统的 SAF 方式（DocumentProvider 那套）来处理，返回的时 Document Uri
 *
 * > MediaStore Uri 和 Document Uri 是可以互转的，可以使用 [getDocumentUri](https://developer.android.com/reference/android/provider/MediaStore#getDocumentUri(android.content.Context,%20android.net.Uri)) 来处理
 */
@RequiresApi(Build.VERSION_CODES.Q)
object ExternalStorageHelper {

}