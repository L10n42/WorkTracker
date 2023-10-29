package com.kappdev.worktracker.tracker_feature.domain.use_case

import android.content.Context
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ZipDatabase @Inject constructor(
    private val context: Context,
    private val database: WorkDatabase
) {

    operator fun invoke(): File {
        if (database.isOpen) {
            database.close()
        }

        val zipFile = File(context.cacheDir, "work_tracker_db.zip")
        val sourceFiles = WorkDatabase.getFilePaths(context).map { File(it) }

        createZipFile(zipFile, sourceFiles)
        return zipFile
    }

    private fun createZipFile(zipFile: File, sourceFiles: List<File>) {
        try {
            ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { output ->
                sourceFiles.forEach { file ->
                    if (file.length() > 0) {
                        FileInputStream(file).use { input ->
                            BufferedInputStream(input).use { origin ->
                                val entry = ZipEntry(file.name)
                                output.putNextEntry(entry)
                                origin.copyTo(output, 1024)
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}