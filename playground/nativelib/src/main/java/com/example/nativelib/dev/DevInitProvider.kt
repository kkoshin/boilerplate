package com.example.nativelib.dev

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * 借助 content provider 来隐式初始化，也可以使用 FileProvider 这类 contentProvider 的子类
 * 注意事项：这种方式不太好控制初始化顺序，所以业务类的不应该使用这种，只适合那种没有依赖项也没有被依赖的工具类SDK
 *
 * 在主工程里可以调整 `android:initOrder` （越大越先初始化）来控制初始化顺序，当然更高效的方式是共享一个 content provider 的方式去初始化
 *
 * 另外，这种初始化可以被禁用
 */
class DevInitProvider: ContentProvider() {
    override fun onCreate(): Boolean {
        DevShortcut.init(context!!.applicationContext)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0
}