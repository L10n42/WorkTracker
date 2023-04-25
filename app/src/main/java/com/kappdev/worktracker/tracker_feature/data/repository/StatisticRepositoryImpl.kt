package com.kappdev.worktracker.tracker_feature.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kappdev.worktracker.tracker_feature.data.data_source.SessionDao
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateTimeHelper
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class StatisticRepositoryImpl(
    private val sessionDao: SessionDao
): StatisticRepository {

    override fun getDailySessionsFor(activityId: Long, date: LocalDate): List<Session> {
        val query = "SELECT * FROM sessions WHERE activityId = ?"
        val params = arrayOf(activityId)
        val simpleSqliteQuery = SimpleSQLiteQuery(query, params)
        val sessionsList = sessionDao.getSessionsFor(simpleSqliteQuery)

        return sessionsList.mapNotNull { session ->
            val instant = Instant.ofEpochMilli(session.startTimestamp)
            val startDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

            if (startDate == date && session.endTimestamp != 0L) session else null
        }
    }
}