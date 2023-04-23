package com.kappdev.worktracker.tracker_feature.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.kappdev.worktracker.tracker_feature.data.data_source.ActivityDao
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import kotlinx.coroutines.flow.Flow

class ActivityRepositoryImpl(
    private val activityDao: ActivityDao
): ActivityRepository {

    override fun getSorted(order: ActivityOrder): List<Activity> {
        val query = SimpleSQLiteQuery(
            "SELECT * FROM activities ORDER BY ${order.id} ${order.orderType.id}"
        )
        return activityDao.getWithOrder(query)
    }

    override suspend fun insertActivity(activity: Activity): Long {
        return activityDao.insertActivity(activity)
    }

    override fun getActivities(): Flow<List<Activity>> {
        return activityDao.getActivities()
    }

    override fun getActivityById(id: Long): Activity {
        return activityDao.getActivityById(id)
    }

    override fun deleteActivityById(id: Long) {
        activityDao.deleteActivityById(id)
    }

    override suspend fun deleteActivity(activity: Activity): Int {
        return activityDao.deleteActivity(activity)
    }
}