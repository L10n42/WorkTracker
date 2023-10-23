package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activity_id")
    val id: Long = 0,

    @ColumnInfo(name = "activity_name")
    val name: String = "",

    @ColumnInfo(name = "creation_timestamp")
    val creationTimestamp: Long = 0,

    @ColumnInfo(name = "target_in_seconds")
    val targetInSec: Long = 0
)

