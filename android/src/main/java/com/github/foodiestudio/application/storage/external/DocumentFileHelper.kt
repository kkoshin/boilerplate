package com.github.foodiestudio.application.storage.external

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import java.io.InputStream

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