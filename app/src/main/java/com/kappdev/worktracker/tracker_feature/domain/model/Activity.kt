package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val creationTimestamp: Long
) {
    companion object {
        val Empty = Activity(id = 0, name = "", creationTimestamp = 0)
    }
}
