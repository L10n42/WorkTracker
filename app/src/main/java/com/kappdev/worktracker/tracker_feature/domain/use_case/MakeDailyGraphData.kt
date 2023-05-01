package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.util.DateTimeHelper
import com.kappdev.worktracker.tracker_feature.domain.util.padKeys
import com.kappdev.worktracker.tracker_feature.domain.util.trimZeros

class MakeDailyGraphData {

    operator fun invoke(data: List<Session>): Map<String, Long> {
        val map = mutableMapOf<Int, Long>()
        for (i in 1..24) { map[i] = 0 }

        if (data.isEmpty()) return map.padKeys()

        fun addTimeAt(hour: Int, time: Long) {
            map[hour] = (map[hour] ?: 0) + time
        }

        data.forEach { session ->
            val startHour = DateTimeHelper.getHourOfDay(session.startTimestamp)
            val endHour = DateTimeHelper.getHourOfDay(session.endTimestamp)

            if (startHour == endHour) {
                addTimeAt(startHour, session.timeInSec)
            } else {
                var time = session.timeInSec
                val hours = mutableListOf<Int>()

                session.minutePoints.timestamps.forEach { timestamp ->
                    val hour = DateTimeHelper.getHourOfDay(timestamp)
                    if (!hours.contains(hour)) hours.add(hour)
                    addTimeAt(hour, 60L)
                    time -= 60L
                }

                if (time > 60) {
                    val averageTime = time / hours.size
                    hours.forEach { hour -> addTimeAt(hour, averageTime) }
                }
            }
        }

        return map.trimZeros().padKeys()
    }
}