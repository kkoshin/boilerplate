package com.github.foodiestudio.application

import android.annotation.TargetApi
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


/**
 * 针对仅应用持久化的文件，涉及 File 和 Cache。应用卸载后，这些数据也会同步被删除。
 *
 * 早期 Android 手机都支持存储卡扩展的，所以也会将应用存储分为两个，一个是空间比较小的内部存储，一个是空间比较大的外部存储。
 * 即便现在很多手机不支持SD卡扩展了，还是沿用了这个设计，只不过没有了实体的SD卡，改为 emulated 的外部存储。
 *
 * - 内部：/data/data/<package_name>/files
 * - 外部：/storage/emulated/0/Android/data/<package_name>/files
 *
 * 这里可以使用完整的 File API，不涉及 Uri 的使用，也没有任何权限的请求。
 */
class AppFileHelper(private val applicationContext: Context) {

    init {
        check(applicationContext is Application) {
            "need valid applicationContext"
        }
    }

    private val internalRoot: File = applicationContext.filesDir
    private val internalCacheRoot: File = applicationContext.cacheDir

    private val externalRoot: File? = applicationContext.getExternalFilesDir(null)
    private val externalCacheRoot: File? = applicationContext.externalCacheDir

    fun requireFilesDir(sensitive: Boolean): File = if (sensitive) {
        internalRoot
    } else {
        externalRoot!!
    }

    fun requireCacheDir(sensitive: Boolean): File = if (sensitive) {
        internalCacheRoot
    } else {
        externalCacheRoot!!
    }

    /**
     * 在 [internalRoot] 创建一个临时文件, 文件名为 "[prefix]随机字符串.temp"
     */
    fun createTempFile(prefix: String = "Cache"): File {
        return File.createTempFile(prefix, null)
    }

    /**
     * @param sensitive 对应获取 [internalCacheRoot] 还是 [externalCacheRoot]
     * @return 剩余 Cache 大小，单位为字节, 如需转为 MB，可以将除以 1024*1024
     */
    @WorkerThread
    fun getCacheQuotaBytes(sensitive: Boolean): Result<Long> {
        val storageManager =
            applicationContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val appSpecificUuid = storageManager.getUuidForPath(requireCacheDir(sensitive))
        return storageManager.runCatching {
            getCacheQuotaBytes(appSpecificUuid)
        }
    }

    /**
     * 往 [internalRoot] 里写入 [fileName] 文件，如果本身不存在这个文件的话，顺带会创建。
     *
     * @param fileName 文件名，例如，Foo.text，不能是 /Foo/Bar.text 这类包含路径的文件名
     */
    @WorkerThread
    @Deprecated("没什么实际用处的API")
    fun writeSensitiveFile(
        fileName: String,
        action: (FileOutputStream) -> Unit
    ) {
        applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            action(it)
        }
    }

    /**
     * [writeSensitiveFile] 的读取版本，如果本身不存在这个文件的话，顺带会创建。
     */
    @WorkerThread
    @Deprecated("没什么实际用处的API")
    fun readSensitiveFile(fileName: String): String {
        return applicationContext.openFileInput(fileName).bufferedReader().readLines()
            .joinToString("\n")
    }
}

/**
 * 这里只讨论不需要权限请求的前提下。
 * 存储在应用外的共享文件夹中，独立于应用的生命周期，即便应用卸载了，这些文件也不会被删除
 *
 * - 不涉及 Cache
 * - 针对媒体文件需要使用 MediaStore 来处理
 * - 非媒体文件需要采用传统的 SAF 方式（FileProvider 那套）来处理
 */
@RequiresApi(Build.VERSION_CODES.Q)
object ExternalStorageHelper {

}

/**
 * 支持对 Virtual File 的处理
 */
class DocumentFileHelper(private val applicationContext: Context) {

    @TargetApi(Build.VERSION_CODES.N)
    private fun isVirtualFile(uri: Uri): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return false

        if (!DocumentsContract.isDocumentUri(applicationContext, uri)) {
            return false
        }

        applicationContext.contentResolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_FLAGS),
            null, null, null
        )?.use {
            return if (it.moveToFirst()) {
                (it.getInt(0) and DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0
            } else {
                false
            }
        } ?: return false
    }

    private fun getInputStreamForVirtualFile(uri: Uri, mimeTypeFilter: String): InputStream? {
        val resolver: ContentResolver = applicationContext.contentResolver

        val openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter)

        if (openableMimeTypes.isNullOrEmpty()) {
            return null
        }

        return resolver.openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
            ?.createInputStream()
    }

    // alternativeMimeType 处理 virtualFile 时需要传入的 mimeType，例如，"image/*"
    fun getInputStream(uri: Uri, alternativeMimeType: String? = null): InputStream? {
        if (isVirtualFile(uri) && alternativeMimeType != null) {
            return getInputStreamForVirtualFile(uri, alternativeMimeType)
        }
        return applicationContext.contentResolver.openInputStream(uri)
    }
}