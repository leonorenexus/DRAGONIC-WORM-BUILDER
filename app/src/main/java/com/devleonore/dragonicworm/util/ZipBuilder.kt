package com.devleonore.dragonicworm.util

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Compresses a map of relative-path -> text content into a ZIP file
 * rooted under [rootFolder]. Uses only java.util.zip — no third-party
 * library, fully offline.
 */
object ZipBuilder {

    fun build(outputFile: File, rootFolder: String, files: Map<String, String>) {
        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            for ((relativePath, content) in files) {
                val entryPath = "$rootFolder/$relativePath"
                val entry = ZipEntry(entryPath)
                zos.putNextEntry(entry)
                zos.write(content.toByteArray(Charsets.UTF_8))
                zos.closeEntry()
            }
        }
    }
}
