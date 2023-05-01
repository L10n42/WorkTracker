package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import com.kappdev.worktracker.tracker_feature.domain.util.getWeek
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class GetWeekDataFor @Inject constructor(
    private val repository: StatisticRepository
) {

    operator fun invoke(activityId: Long, date: LocalDate): Map<String, Long> {
        val week = date.getWeek()
        val map = emptyWeekMap(week)

        val sessions = repository.getSessionsForPeriod(activityId, week)
        if (sessions.isEmpty()) return map.toWeekMap()

        sessions.forEach { session ->
            val sessionDate = DateUtil.getDateOf(session.startTimestamp)
            map[sessionDate] = (map[sessionDate] ?: 0) + session.timeInSec
        }

        return map.toWeekMap()
    }

    private fun MutableMap<LocalDate, Long>.toWeekMap(): Map<String, Long> {
        return this.mapKeys { entry ->
            entry.key.dayOfWeek.name
                .substring(0, 3)
                .lowercase()
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
        }
    }

    private fun emptyWeekMap(week: Pair<LocalDate, LocalDate>): MutableMap<LocalDate, Long> {
        val weekMap = mutableMapOf<LocalDate, Long>()
        var currentDate = week.first

        while (currentDate <= week.second) {
            weekMap[currentDate] = 0
            currentDate = currentDate.plusDays(1)
        }

        return weekMap
    }
}