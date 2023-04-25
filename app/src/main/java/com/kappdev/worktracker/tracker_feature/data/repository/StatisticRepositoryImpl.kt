package com.kappdev.worktracker.tracker_feature.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kappdev.worktracker.tracker_feature.data.data_source.SessionDao
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateTimeHelper

class StatisticRepositoryImpl(
    private val sessionDao: SessionDao
): StatisticRepository {

    override fun getDailySessionsFor(activityId: Long): List<Session> {
        val query = "SELECT * FROM sessions WHERE activityId = ?"
        val params = arrayOf(activityId)
        val simpleSqliteQuery = SimpleSQLiteQuery(query, params)
        val sessionsList = sessionDao.getSessionsFor(simpleSqliteQuery)

        return sessionsList.mapNotNull { session ->
            if (DateTimeHelper.isCurrentDay(session.startTimestamp) && session.endTimestamp != 0L) {
                session
            } else {
                null
            }
        }
    }
}