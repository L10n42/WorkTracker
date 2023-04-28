package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kappdev.worktracker.tracker_feature.data.util.MinutePointsConvertor

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "activity_id")
    val activityId: Long,

    @ColumnInfo(name = "start_timestamp")
    val startTimestamp: Long,

    @ColumnInfo(name = "end_timestamp")
    val endTimestamp: Long,

    @ColumnInfo(name = "time_in_seconds")
    val timeInSec: Long,

    @ColumnInfo(name = "minute_points")
    @TypeConverters(MinutePointsConvertor::class)
    val minutePoints: MinutePoints
)
