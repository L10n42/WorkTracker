package com.kappdev.worktracker.tracker_feature.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kappdev.worktracker.tracker_feature.data.data_source.SessionDao
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import java.time.LocalDate

class StatisticRepositoryImpl(
    private val sessionDao: SessionDao
): StatisticRepository {

    override fun getDailySessionsFor(activityId: Long, date: LocalDate): List<Session> {
        val query = "SELECT * FROM sessions WHERE activityId = ?"
        val params = arrayOf(activityId)
        val simpleSqliteQuery = SimpleSQLiteQuery(query, params)
        val sessionsList = sessionDao.getSessionsFor(simpleSqliteQuery)

        return sessionsList.mapNotNull { session ->
            val startDate = DateUtil.getDateOf(session.startTimestamp)
            if (startDate == date && session.endTimestamp != 0L) session else null
        }
    }

    override fun getMonthSessionsFor(activityId: Long, date: LocalDate): List<Session> {
        val sessions = sessionDao.getSessionsByActivity(activityId)

        return sessions.mapNotNull { session ->
            val startDate = DateUtil.getDateOf(session.startTimestamp)
            if (isSameMonth(startDate, date) && session.endTimestamp != 0L) session else null
        }
    }

    private fun isSameMonth(first: LocalDate, second: LocalDate): Boolean {
        return first.year == second.year && first.month == second.month
    }
}