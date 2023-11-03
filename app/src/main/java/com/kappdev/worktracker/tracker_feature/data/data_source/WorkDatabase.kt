package com.kappdev.worktracker.tracker_feature.data.data_source

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.Session

@Database(
    entities = [Activity::class, Session::class],
    version = 10,
    autoMigrations = [
        AutoMigration (from = 8, to = 9),
        AutoMigration (from = 9, to = 10)
    ]
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

val WorkDatabase.Companion.MIGRATION_8_10: Migration
    get() = object : Migration(8, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a temporary table with the new schema.
            database.execSQL("CREATE TABLE IF NOT EXISTS activities_temp " +
                    "(activity_id INTEGER PRIMARY KEY NOT NULL, " +
                    "activity_name TEXT NOT NULL, " +
                    "target_in_seconds INTEGER NOT NULL, " +
                    "creation_timestamp INTEGER NOT NULL, " +
                    "new_column TEXT)")

            // Copy the data from the old table to the temporary table.
            database.execSQL("INSERT INTO activities_temp " +
                    "(activity_id, activity_name, target_in_seconds, creation_timestamp, new_column) " +
                    "SELECT activity_id, activity_name, target_in_seconds, creation_timestamp, 'default_value' " +
                    "FROM activities")

            // Drop the old table.
            database.execSQL("DROP TABLE activities")

            // Rename the temporary table to the original table name.
            database.execSQL("ALTER TABLE activities_temp RENAME TO activities")
        }
    }