package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {

    fun getSorted(order: ActivityOrder): List<Activity>

    suspend fun insertActivity(activity: Activity): Long

    fun getActivities(): Flow<List<Activity>>

    fun getActivityById(id: Long): Activity?

    fun deleteActivityById(id: Long)

    suspend fun deleteActivity(activity: Activity): Int

}