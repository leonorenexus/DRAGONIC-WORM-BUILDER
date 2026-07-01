package com.devleonore.dragonicworm.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream

/**
 * Handles writing the generated ZIP to the public Downloads folder
 * and building a Share intent. All local — no network calls.
 */
object FileExporter {

    /**
     * Copies [sourceZip] (already built in app cache) into the public
     * Downloads collection using MediaStore (required on API 29+ scoped storage).
     * Returns the content Uri of the saved file, or null on failure.
     */
    fun saveToDownloads(context: Context, sourceZip: File, displayName: String): Uri? {
        return try {
            val resolver = context.contentResolver
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, displayName)
                put(MediaStore.Downloads.MIME_TYPE, "application/zip")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
            val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val itemUri = resolver.insert(collection, values) ?: return null

            resolver.openOutputStream(itemUri)?.use { out ->
                FileInputStream(sourceZip).use { input ->
                    input.copyTo(out)
                }
            }

            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(itemUri, values, null, null)

            itemUri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Builds a share Intent (chooser) for the given ZIP file via FileProvider. */
    fun buildShareIntent(context: Context, zipFile: File): Intent {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            zipFile
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return Intent.createChooser(shareIntent, "Share project ZIP")
    }

    /** Returns the app's cache directory for staging the ZIP before save/share. */
    fun cacheDir(context: Context): File {
        val dir = File(context.cacheDir, "exports")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }
}
