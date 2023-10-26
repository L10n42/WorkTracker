package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "session_id")
    val id: Long,

    @ColumnInfo(name = "activity_id")
    val activityId: Long,

    @ColumnInfo(name = "start_timestamp")
    val startTimestamp: Long,

    @ColumnInfo(name = "end_timestamp")
    val endTimestamp: Long
)

fun Session.getDurationInSecond(): Long {
    return (this.endTimestamp - this.startTimestamp) / 1000
}
