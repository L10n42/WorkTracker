package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.util.DateTimeHelper
import com.kappdev.worktracker.tracker_feature.domain.util.trimZeros

class MakeDailyGraphData {

    operator fun invoke(data: List<Session>): Map<Int, Long> {
        val map = mutableMapOf<Int, Long>()
        for (i in 1..24) { map[i] = 0 }

        if (data.isEmpty()) return map

        fun addTimeAt(hour: Int, time: Long) {
            map[hour] = (map[hour] ?: 0) + time
        }

        data.forEach { session ->
            val startHour = DateTimeHelper.getHourOfDay(session.startTimestamp)
            val endHour = DateTimeHelper.getHourOfDay(session.endTimestamp)

            if (startHour == endHour) {
                addTimeAt(startHour, session.timeInSec)
            } else {
                val hours = getHoursBetween(startHour, endHour)
                val averageTime = session.timeInSec / hours.size

                hours.forEach { hour -> addTimeAt(hour, averageTime) }
            }
        }

        return map.trimZeros()
    }

    private fun getHoursBetween(start: Int, end: Int): List<Int> {
        val hours = mutableListOf<Int>()
        for (i in start..end) { hours.add(i) }

        return hours
    }
}