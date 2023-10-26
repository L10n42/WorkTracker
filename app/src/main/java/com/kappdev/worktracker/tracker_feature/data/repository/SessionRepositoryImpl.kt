package com.kappdev.worktracker.tracker_feature.data.repository

import com.kappdev.worktracker.tracker_feature.data.data_source.SessionDao
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import java.time.LocalDate
import java.time.ZoneId

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
): SessionRepository {

    override suspend fun saveSession(id: Long) {
        val session = sessionDao.getSessionById(id).copy(
            endTimestamp = System.currentTimeMillis()
        )
        sessionDao.insertSession(session)
    }

    override suspend fun startSessionFor(activityId: Long): Long {
        return sessionDao.insertSession(
            Session(
                id = 0,
                activityId = activityId,
                startTimestamp = System.currentTimeMillis(),
                endTimestamp = 0
            )
        )
    }

    override fun getSessionForDate(date: LocalDate): List<Session> {
        val startOfDay = date.atStartOfDay(ZoneId.systemDefault())
        val endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault())
        val startTimestamp = startOfDay.toInstant().toEpochMilli()
        val endTimestamp = endOfDay.toInstant().toEpochMilli()
        return sessionDao.getForPeriod(startTimestamp, endTimestamp)
    }

    override fun deleteSessionsByActivity(id: Long) {
        sessionDao.deleteSessionsByActivity(id)
    }
}