package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.getDurationInSecond
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.util.DateTimeHelper.getHourOfDay
import com.kappdev.worktracker.tracker_feature.domain.util.DateTimeHelper.getMinuteOfHour
import com.kappdev.worktracker.tracker_feature.domain.util.padKeys
import com.kappdev.worktracker.tracker_feature.domain.util.trimZeros
import java.time.LocalDate
import javax.inject.Inject

class GetDayDataFor @Inject constructor(
    private val repository: StatisticRepository
) {

    operator fun invoke(activityId: Long, date: LocalDate): Map<String, Long> {
        val map = (1..24).associateWith { 0L }.toMutableMap()

        val sessions = repository.getForDay(activityId, date)
        if (sessions.isEmpty()) return map.padKeys()

        fun addTimeAt(hour: Int, time: Long) {
            map[hour] = (map[hour] ?: 0) + time
        }

        sessions.forEach { session ->
            val startHour = getHourOfDay(session.startTimestamp)
            val endHour = getHourOfDay(session.endTimestamp)

            if (startHour == endHour) {
                addTimeAt(startHour, session.getDurationInSecond())
            } else {
                val startHourSecond = (60 - getMinuteOfHour(session.startTimestamp)) * 60L
                val endHourSecond = getMinuteOfHour(session.endTimestamp) * 60L

                addTimeAt(startHour, startHourSecond)
                addTimeAt(endHour, endHourSecond)

                for (hour in (startHour + 1) until endHour) {
                    addTimeAt(hour, 3600L)
                }
            }
        }

        return map.trimZeros().padKeys()
    }
}