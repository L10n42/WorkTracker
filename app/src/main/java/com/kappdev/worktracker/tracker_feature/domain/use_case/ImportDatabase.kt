package com.kappdev.worktracker.tracker_feature.domain.use_case

import android.content.Context
import android.net.Uri
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase.Companion.NAME
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.util.FileUtil
import com.kappdev.worktracker.tracker_feature.domain.util.ZipManager
import dagger.hilt.android.scopes.ViewModelScoped
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@ViewModelScoped
class ImportDatabase @Inject constructor(
    private val context: Context,
    private val repository: ActivityRepository
) {

    private val cacheDirPath = context.cacheDir.path + File.separator
    private lateinit var dbZipFile: File
    private lateinit var dbFolderPath: String
    private lateinit var dbFilesPaths: Array<String>

    operator fun invoke(uri: Uri): String {
        val cacheFile = cacheZipFile(uri)
        if (cacheFile != null && cacheFile.exists()) {
            dbZipFile = cacheFile
        } else {
            return "Couldn't get the file."
        }

        if (!ZipManager.zipContainsAll(dbZipFile, WorkDatabase.dbFileNames)) {
            dbZipFile.deleteOnExit()
            return "Unable to find the database."
        }

        val dbFile = context.getDatabasePath(NAME)
        dbFile.mkdirs()
        dbFolderPath = dbFile.path.substringBeforeLast(File.separator) + File.separator

        dbFilesPaths = WorkDatabase.getFilePaths(context).toTypedArray()
        backupCurrentDb()
        ZipManager.unzip(dbZipFile, dbFolderPath)

        if (!validateDb()) {
            return "Unable to integrate the database."
        }

        return "The database integrated successfully."
    }

    private fun backupCurrentDb() {
        dbFilesPaths.forEach { path ->
            FileUtil.moveFile(dbFolderPath, getFileName(path), cacheDirPath)
        }
    }

    private fun validateDb(): Boolean {
        return if (repository.isNotEmpty()) {
            deleteBackupDb()
            dbZipFile.deleteOnExit()
            true
        } else {
            deleteDbFiles()
            restoreBackupDb()
            dbZipFile.deleteOnExit()
            false
        }
    }

    private fun deleteDbFiles() {
        dbFilesPaths.forEach { path ->
            File(path).deleteOnExit()
        }
    }

    private fun deleteBackupDb() {
        dbFilesPaths.forEach { path ->
            val backupFile = File(cacheDirPath + getFileName(path))
            backupFile.deleteOnExit()
        }
    }

    private fun restoreBackupDb() {
        dbFilesPaths.forEach { path ->
            FileUtil.moveFile(cacheDirPath, getFileName(path), dbFolderPath)
        }
    }

    private fun getFileName(path: String): String {
        val file = File(path)
        return file.name
    }

    private fun cacheZipFile(uri: Uri): File? {
        val cacheDir = context.cacheDir
        val destinationFile = File(cacheDir, FileUtil.getFileName(context, uri))

        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                    }
                    output.flush()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return destinationFile
    }
}