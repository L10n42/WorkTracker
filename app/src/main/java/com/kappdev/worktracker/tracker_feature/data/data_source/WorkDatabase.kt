package com.kappdev.worktracker.tracker_feature.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.Session

@Database(
    entities = [Activity::class, Session::class],
    version = 7,
    exportSchema = false
)
abstract class WorkDatabase: RoomDatabase() {

    abstract val activityDao: ActivityDao
    abstract val sessionDao: SessionDao

    companion object {
        private const val SUFFIX_SHM = "-shm"
        private const val SUFFIX_WAL = "-wal"

        const val NAME = "work_database"

        private const val DATABASE_SHM = NAME + SUFFIX_SHM
        private const val DATABASE_WAL = NAME + SUFFIX_WAL

        val dbFileNames = arrayOf(NAME, DATABASE_SHM, DATABASE_WAL)

        fun getFilePaths(context: Context): List<String> {
            val dbPath = context.getDatabasePath(NAME).path
            val dbShm = dbPath + SUFFIX_SHM
            val dbWal = dbPath + SUFFIX_WAL
            return listOf(dbPath, dbShm, dbWal)
        }
    }
}