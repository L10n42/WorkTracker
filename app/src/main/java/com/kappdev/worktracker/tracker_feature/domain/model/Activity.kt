package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kappdev.worktracker.ui.theme.ActivityColors
import com.kappdev.worktracker.ui.theme.DEFAULT_ACTIVITY_COLOR

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activity_id")
    val id: Long = 0,

    @ColumnInfo(name = "activity_name")
    val name: String = "",

    @ColumnInfo(name = "creation_timestamp")
    val creationTimestamp: Long = 0,

    @ColumnInfo(name = "activity_color", defaultValue = DEFAULT_ACTIVITY_COLOR)
    val color: Int = 0,

    @ColumnInfo(name = "target_in_seconds")
    val targetInSec: Long = 0
)

fun Activity.getColor() = Color(this.color)

