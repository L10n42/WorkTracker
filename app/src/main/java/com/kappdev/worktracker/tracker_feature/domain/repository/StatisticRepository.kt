package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.Session
import java.time.LocalDate

interface StatisticRepository {

    fun getDailySessionsFor(activityId: Long, date: LocalDate = LocalDate.now()): List<Session>

    fun getMonthSessionsFor(activityId: Long, date: LocalDate): List<Session>

    fun getSessionsForPeriod(activityId: Long, period: Pair<LocalDate, LocalDate>): List<Session>

}