package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "creation_timestamp")
    val creationTimestamp: Long,

    @ColumnInfo(name = "target_in_seconds")
    val targetInSec: Long
){
    companion object { /* Empty Initialization */ }
}

val Activity.Companion.Empty: Activity
    get() = Activity(id = 0, name = "", creationTimestamp = 0, targetInSec = 0)

