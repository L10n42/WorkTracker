package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.Session
import java.time.LocalDate

interface StatisticRepository {

    fun getForDay(activityId: Long, date: LocalDate = LocalDate.now()): List<Session>

    fun getForMonth(activityId: Long, date: LocalDate): List<Session>

    fun getForPeriod(activityId: Long, period: Pair<LocalDate, LocalDate>): List<Session>

    fun getForYear(activityId: Long, year: Int): List<Session>

}