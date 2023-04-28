package com.kappdev.worktracker.tracker_feature.data.repository

import com.kappdev.worktracker.tracker_feature.data.data_source.SessionDao
import com.kappdev.worktracker.tracker_feature.domain.model.MinutePoints
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
): SessionRepository {

    override suspend fun insertSession(session: Session): Long {
        return sessionDao.insertSession(session)
    }

    override suspend fun saveSession(id: Long, timeInSec: Long, minutePoints: MinutePoints) {
        val session = sessionDao.getSessionById(id).copy(
            endTimestamp = System.currentTimeMillis(),
            timeInSec = timeInSec,
            minutePoints = minutePoints
        )
        sessionDao.insertSession(session)
    }

    override suspend fun startSessionFor(activityId: Long): Long {
        return sessionDao.insertSession(
            Session(
                id = 0,
                activityId = activityId,
                startTimestamp = System.currentTimeMillis(),
                endTimestamp = 0,
                timeInSec = 0,
                minutePoints = MinutePoints.Empty
            )
        )
    }

    override fun getSessionById(id: Long): Session {
        return sessionDao.getSessionById(id)
    }

    override fun getSessionsByActivity(id: Long): List<Session> {
        return sessionDao.getSessionsByActivity(id)
    }

    override fun deleteSessionsByActivity(id: Long) {
        sessionDao.deleteSessionsByActivity(id)
    }

    override fun deleteSessionById(id: Long) {
        sessionDao.deleteSessionById(id)
    }

    override suspend fun deleteSession(session: Session): Int {
        return sessionDao.deleteSession(session)
    }
}