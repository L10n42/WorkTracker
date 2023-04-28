package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.MinutePoints
import com.kappdev.worktracker.tracker_feature.domain.model.Session

interface SessionRepository {

    suspend fun insertSession(session: Session): Long

    suspend fun saveSession(id: Long, timeInSec: Long, minutePoints: MinutePoints)

    suspend fun startSessionFor(activityId: Long): Long

    fun getSessionById(id: Long): Session

    fun getSessionsByActivity(id: Long): List<Session>

    fun deleteSessionsByActivity(id: Long)

    fun deleteSessionById(id: Long)

    suspend fun deleteSession(session: Session): Int
}