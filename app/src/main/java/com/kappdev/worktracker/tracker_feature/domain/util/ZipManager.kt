package com.kappdev.worktracker.tracker_feature.domain.util

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile

object ZipManager {
    const val BUFFER_SIZE = 1024

    fun zipContainsAll(zipFile: File, filesNames: Array<String>): Boolean {
        filesNames.forEach { fileName ->
            if (!zipContains(zipFile, fileName)) return false
        }
        return true
    }

    fun zipContains(zipFile: File, fileName: String): Boolean {
        try {
            ZipFile(zipFile).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    val name = entry.name
                    if (name == fileName) return true
                }
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun unzip(zipFile: File, destDirectory: String) {
        createDirIfDoesNotExists(destDirectory)
        try {
            ZipFile(zipFile).use { zip ->

                zip.entries().asSequence().forEach { entry ->

                    zip.getInputStream(entry).use { input ->
                        val filePath = destDirectory + File.separator + entry.name
                        if (!entry.isDirectory) {
                            extractFile(input, filePath)
                        } else {
                            val dir = File(filePath)
                            dir.mkdir()
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createDirIfDoesNotExists(dir: String) {
        File(dir).run {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    private fun extractFile(inputStream: InputStream, destFilePath: String) {
        try {
            val bos = BufferedOutputStream(FileOutputStream(destFilePath))
            val bytesIn = ByteArray(BUFFER_SIZE)
            var read: Int
            while (inputStream.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}