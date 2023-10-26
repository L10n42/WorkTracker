package com.kappdev.worktracker.tracker_feature.domain.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

object FileUtil {

    fun moveFile(inputPath: String, inputFileName: String, outputPath: String) {
        try {
            val dir = File(outputPath)
            if (!dir.exists()) dir.mkdirs()

            val inputStream = FileInputStream(inputPath + inputFileName)
            val out = FileOutputStream(outputPath + inputFileName)
            val buffer = ByteArray(1024)

            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            inputStream.close()

            out.flush()
            out.close()

            File(inputPath + inputFileName).delete()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme.equals("content")) {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    result = cursor.getString(nameIndex)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            result?.let { res ->
                val cut: Int = res.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = res.substring(cut + 1)
                }
            }
        }
        return result ?: ""
    }
}