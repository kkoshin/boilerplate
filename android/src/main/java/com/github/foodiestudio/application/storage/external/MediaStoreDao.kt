package com.github.foodiestudio.application.storage.external

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import java.io.OutputStream
import kotlin.time.Duration.Companion.minutes

data class Video(
    val uri: Uri,
    val name: String,
    val duration: Int,
    val size: Int
)

@WorkerThread
class MediaStoreDao(private val resolver: ContentResolver) {

    /**
     * 举个例子
     * 查询所有时长大于 [durationInMin] 分钟的视频
     */
    fun queryVideos(durationInMin: Long): List<Video> {
        return MediaStoreQueryBuilder(resolver)
            .find { MediaStore.Video.Media.DURATION greaterEq durationInMin.minutes }
            .collect {
                Video(contentUri, name, duration, size)
            }
    }

    fun moveSong(songUri: Uri, newRelativePath: String) {
        MediaStoreUpdateBuilder(resolver).update(songUri) {
            put(MediaStore.Audio.Media.RELATIVE_PATH, newRelativePath)
        }
    }

    fun deleteSong(songUri: Uri) {

    }

    /**
     * 先插入一条记录，然后根据返回的 uri，去写入内容。
     *
     * 这个过程不知道具体的存放位置
     *
     * @param relativePath 如果不想在 [Environment.DIRECTORY_MUSIC] 根目录下存放，可以再指定一个相对路径，例如：Foo, 最终这个文件会放在 .../Music/Foo 下
     * @return 返回插入的这条记录的 uri，方便后续继续编辑
     */
    // 这个主要是为了更新媒体数据库，方便在相册等地方刷新新加入的。另一方面，如果不希望被扫描出来，记得在对应的文件夹下放入 .noMedia 文件
    fun insertSong(
        fileName: String,
        relativePath: String? = null,
        writer: (OutputStream) -> Unit
    ): Boolean {
        val uri = MediaStoreInsertBuilder(resolver).new {
            put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Audio.Media.IS_PENDING, 1)
            if (!relativePath.isNullOrEmpty()) {
                put(MediaStore.Audio.Media.RELATIVE_PATH, relativePath)
            }
        } ?: return false
        resolver.openOutputStream(uri)?.use {
            writer(it)
        }
        resolver.update(uri, ContentValues().apply {
            put(MediaStore.Audio.Media.IS_PENDING, 0)
        }, null, null)
        return true
    }

//    private fun query(durationInMin: Long, onResult: (Cursor) -> Unit) {
//        val collection =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                MediaStore.Video.Media.getContentUri(
//                    MediaStore.VOLUME_EXTERNAL
//                )
//            } else {
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//            }
//
//        val projection = arrayOf(
//            MediaStore.Video.Media._ID,
//            MediaStore.Video.Media.DISPLAY_NAME,
//            MediaStore.Video.Media.DURATION,
//            MediaStore.Video.Media.SIZE
//        )
//
//        // Show only videos that are at least X minutes in duration.
//        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
//        val selectionArgs = arrayOf(
//            TimeUnit.MILLISECONDS.convert(durationInMin, TimeUnit.MINUTES).toString()
//        )
//
//        // Display videos in alphabetical order based on their display name.
//        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
//
//        val query = resolver.query(
//            collection,
//            projection,
//            selection,
//            selectionArgs,
//            sortOrder
//        )
//        query?.use(onResult)
//    }
//
//    private fun queryAllVideoLonger(durationInMin: Long): List<Video> {
//        val videoList = mutableListOf<Video>()
//        query(durationInMin) { cursor ->
//            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
//            val nameColumn =
//                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
//            val durationColumn =
//                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
//            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
//
//            while (cursor.moveToNext()) {
//                // Get values of columns for a given video.
//                val id = cursor.getLong(idColumn)
//                val name = cursor.getString(nameColumn)
//                val duration = cursor.getInt(durationColumn)
//                val size = cursor.getInt(sizeColumn)
//
//                val contentUri: Uri = ContentUris.withAppendedId(
//                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                    id
//                )
//
//                // Stores column values and the contentUri in a local object
//                // that represents the media file.
//                videoList += Video(contentUri, name, duration, size)
//            }
//        }
//        return videoList
//    }

}