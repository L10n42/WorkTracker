package com.kappdev.worktracker.tracker_feature.domain.use_case

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.kappdev.worktracker.BuildConfig.APPLICATION_ID
import com.kappdev.worktracker.core.domain.util.Result
import com.kappdev.worktracker.core.domain.util.fail
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.domain.util.ZipManager
import javax.inject.Inject

class ShareDatabase @Inject constructor(
    private val context: Context,
    private val zipDatabase: ZipDatabase
) {
    operator fun invoke(): Result<Unit> {
        val zipFile = zipDatabase()

        if (!ZipManager.zipContainsAll(zipFile, WorkDatabase.dbFileNames)) {
            zipFile.deleteOnExit()
            return Result.fail("Couldn't export the database.")
        }

        val zipFileUri = FileProvider.getUriForFile(context, "$APPLICATION_ID.provider", zipFile)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/zip"
        shareIntent.putExtra(Intent.EXTRA_STREAM, zipFileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val chooserIntent = Intent.createChooser(shareIntent, "Share Database")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)

        return Result.Success(Unit)
    }
}