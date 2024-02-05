package com.github.foodiestudio.application.storage.external

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import java.io.InputStream

/**
 * 这里仅访问 **自己的应用创建的** 文件，所以不需要权限。
 *
 * > 注意：如果用户卸载并重新安装您的应用，您必须请求 READ_EXTERNAL_STORAGE 才能访问应用最初创建的文件。此权限请求是必需的，
 *
 * 该框架提供经过优化的媒体集合索引，称为媒体库，使您可以更轻松地检索和更新这些媒体文件。即使您的应用已卸载，这些文件仍会保留在用户的设备上。
 *
 * 本质上这个就是一个数据库，所以现在还需要比较使用 类SQL 的方式去调用
 */
@RequiresApi(Build.VERSION_CODES.Q)
internal class MediaFileHelper(applicationContext: Context) {

    private val contentResolver = applicationContext.contentResolver

    init {
        check(applicationContext is Application) {
            "require valid applicationContext"
        }
    }

    /**
     * 媒体库版本, 就像数据库版本那样，一般发生比较大的表结构调整后才会调整。
     * 如果这个版本发生了变化，请重新扫描并重新同步应用的媒体缓存。
     *
     * 请在应用进程启动时完成此项检查。您无需在每次查询媒体库时都检查版本。
     * 和数据库一样，数据的插入并不会影响这个版本号。
     */
    val mediaStoreVersion = MediaStore.getVersion(applicationContext)

    /**
     * MediaStore.Image : 覆盖了 DCIM/ 和 Pictures/
     */
    fun fetchImages() {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    /**
     * MediaStore.Video: 覆盖了 DCIM/、Movies/ 和 Pictures/
     */
    fun fetchVideos() {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    /**
     * MediaStore.Audio: 覆盖了 Alarms/、Audiobooks/、Music/、Notifications/、Podcasts/ 和 Ringtones/ 以及 Movies/ 目录中的音频播放列表，以及 Recordings/ 目录中的录音（Recordings/ 目录在 Android 11（API 级别 30）及更低版本中不可用。）
     */
    fun fetchAudios() {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    /**
     * Download/ 目录中的文件。但仅限自己创建的，如果是其他的应用的文件，只能通过 SAF 的方式（也就是需要用户介入）
     * Android 10及以上可用
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun fetchDownloadFiles() {
        MediaStore.Downloads.EXTERNAL_CONTENT_URI
    }

    /**
     * Android 10 及更高
     * 如果启用了分区存储，集合只会显示您的应用创建的照片、视频和音频文件。大多数开发者无需使用 MediaStore.Files 即可查看其他应用的媒体文件，但如果您有特定要求，则可以声明 READ_EXTERNAL_STORAGE 权限。不过，我们建议您使用 MediaStore API 打开您的应用尚未创建的文件。
     * 如果分区存储不可用或未使用，集合将显示所有类型的媒体文件。
     */
    @Deprecated("开启分区存储后，无法查看所有类型的媒体文件")
    fun fetchFiles() {

    }

    /**
     * 获取 [mediaUri] 对应的 [width] * [height] 大小的缩略图
     */
    fun loadThumbnail(mediaUri: Uri, width: Int, height: Int): Bitmap {
        return contentResolver.loadThumbnail(
            mediaUri, Size(width, height), null
        )
    }

    fun open(mediaUri: Uri): Result<InputStream?> {
        return runCatching {
            contentResolver.openInputStream(mediaUri)
        }
    }

    fun open(mediaUri: Uri, mode: FileMode): Result<ParcelFileDescriptor?> {
        return runCatching {
            contentResolver.openFileDescriptor(mediaUri, mode.value)
        }
    }
}

// "r", "w", "wt", "wa", "rw" or "rwt".
enum class FileMode(val value: String) {
    READ("r"),
    WRITE("w"),
    WRITE_TRUNCATE("wt"),
    WRITE_APPEND("wa"),
    READ_WRITE("rw"),
    READ_WRITE_TRUNCATE("rwt");
}