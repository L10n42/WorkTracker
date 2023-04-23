package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.Session

interface SessionRepository {

    suspend fun insertSession(session: Session): Long

    fun getSessionById(id: Long): Session

    fun getSessionsByActivity(id: Long): List<Session>

    fun deleteSessionsByActivity(id: Long)

    fun deleteSessionById(id: Long)

    suspend fun deleteSession(session: Session): Int
}