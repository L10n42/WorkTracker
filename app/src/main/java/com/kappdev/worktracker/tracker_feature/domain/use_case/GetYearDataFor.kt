package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.getDurationInSecond
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import com.kappdev.worktracker.tracker_feature.domain.util.getName
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

class GetYearDataFor @Inject constructor(
    private val repository: StatisticRepository
) {

    operator fun invoke(activityId: Long, date: LocalDate): Map<String, Long> {
        val map = emptyYearMap()

        val sessions = repository.getForYear(activityId, date.year)
        if (sessions.isEmpty()) return map.toYearMap()

        sessions.forEach { session ->
            val sessionMonth = DateUtil.getDateOf(session.startTimestamp).monthValue
            map[sessionMonth] = (map[sessionMonth] ?: 0) + session.getDurationInSecond()
        }

        return map.toYearMap()
    }

    private fun MutableMap<Int, Long>.toYearMap(): Map<String, Long> {
        return this.mapKeys { entry ->
            Month.of(entry.key).getName().substring(0, 3)
        }
    }

    private fun emptyYearMap(): MutableMap<Int, Long> {
        val map = mutableMapOf<Int, Long>()
        for (i in 1..12) { map[i] = 0 }

        return map
    }
}