package com.kappdev.worktracker.tracker_feature.data.repository

import com.kappdev.worktracker.tracker_feature.data.data_source.SessionDao
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import java.time.LocalDate

class StatisticRepositoryImpl(
    private val sessionDao: SessionDao
): StatisticRepository {

    override fun getForDay(activityId: Long, date: LocalDate): List<Session> {
        val sessionsList = sessionDao.getSessionsByActivity(activityId)

        return sessionsList.mapNotNull { session ->
            val startDate = DateUtil.getDateOf(session.startTimestamp)
            if (startDate == date && session.finished()) session else null
        }
    }

    override fun getForMonth(activityId: Long, date: LocalDate): List<Session> {
        val sessions = sessionDao.getSessionsByActivity(activityId)

        return sessions.mapNotNull { session ->
            val startDate = DateUtil.getDateOf(session.startTimestamp)
            if (isSameMonth(startDate, date) && session.finished()) session else null
        }
    }

    override fun getForPeriod(activityId: Long, period: Pair<LocalDate, LocalDate>): List<Session> {
        val sessions = sessionDao.getSessionsByActivity(activityId)

        return sessions.mapNotNull { session ->
            val date = DateUtil.getDateOf(session.startTimestamp)
            if (inPeriod(period, date) && session.finished()) session else null
        }
    }

    override fun getForYear(activityId: Long, year: Int): List<Session> {
        val sessions = sessionDao.getSessionsByActivity(activityId)

        return sessions.mapNotNull { session ->
            val sessionYear = DateUtil.getDateOf(session.startTimestamp).year
            if (sessionYear == year && session.finished()) session else null
        }
    }

    private fun Session.finished() = this.endTimestamp != 0L

    private fun inPeriod(period: Pair<LocalDate, LocalDate>, date: LocalDate): Boolean {
        return date >= period.first && date <= period.second
    }

    private fun isSameMonth(first: LocalDate, second: LocalDate): Boolean {
        return first.year == second.year && first.month == second.month
    }
}