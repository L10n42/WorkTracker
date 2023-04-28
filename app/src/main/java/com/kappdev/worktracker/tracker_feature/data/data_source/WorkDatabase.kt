package com.kappdev.worktracker.tracker_feature.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kappdev.worktracker.tracker_feature.data.util.MinutePointsConvertor
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.Session

@Database(
    entities = [Activity::class, Session::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(MinutePointsConvertor::class)
abstract class WorkDatabase: RoomDatabase() {

    abstract val activityDao: ActivityDao
    abstract val sessionDao: SessionDao

    companion object {
        const val NAME = "work_database"
    }
}