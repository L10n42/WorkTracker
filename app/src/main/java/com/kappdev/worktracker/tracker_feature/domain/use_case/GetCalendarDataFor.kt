package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import java.time.LocalDate
import javax.inject.Inject

class GetCalendarDataFor @Inject constructor(
    private val repository: StatisticRepository
) {

    operator fun invoke(activityId: Long, date: LocalDate): Map<Int, Long> {
        val sessions = repository.getMonthSessionsFor(activityId, date)

        val map = mutableMapOf<Int, Long>()
        DateUtil.getMonthDays(date).forEach {
            map[it] = 0
        }

        if (sessions.isEmpty()) return map

        fun addTimeAt(day: Int, time: Long) {
            map[day] = (map[day] ?: 0) + time
        }

        sessions.forEach { session ->
            val startDay = DateUtil.getDateOf(session.startTimestamp)
            addTimeAt(startDay.dayOfMonth, session.timeInSec)
        }

        return map
    }
}