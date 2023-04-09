package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val activityId: Long,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val timeInSec: Long
)
