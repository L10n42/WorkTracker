package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.Session
import java.time.LocalDate

interface SessionRepository {

    suspend fun saveSession(id: Long)

    suspend fun startSessionFor(activityId: Long): Long

    fun getSessionForDate(date: LocalDate): List<Session>

    fun deleteSessionsByActivity(id: Long)
}