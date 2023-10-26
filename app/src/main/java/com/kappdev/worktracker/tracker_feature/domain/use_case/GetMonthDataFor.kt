package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.getDurationInSecond
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import com.kappdev.worktracker.tracker_feature.domain.util.emptyPeriodMap
import com.kappdev.worktracker.tracker_feature.domain.util.getMonthPeriod
import java.time.LocalDate
import javax.inject.Inject

class GetMonthDataFor @Inject constructor(
    private val repository: StatisticRepository
) {

    operator fun invoke(activityId: Long, date: LocalDate): Map<String, Long> {
        val month = date.getMonthPeriod()
        val map = month.emptyPeriodMap()

        val sessions = repository.getForPeriod(activityId, month)
        if (sessions.isEmpty()) return map.toMonthMap()

        sessions.forEach { session ->
            val sessionDate = DateUtil.getDateOf(session.startTimestamp)
            map[sessionDate] = (map[sessionDate] ?: 0) + session.getDurationInSecond()
        }

        return map.toMonthMap()
    }

    private fun MutableMap<LocalDate, Long>.toMonthMap(): Map<String, Long> {
        return this.mapKeys { entry ->
            entry.key.dayOfMonth.toString().padStart(2, '0')
        }
    }
}