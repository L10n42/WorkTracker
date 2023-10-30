package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.ReportData
import com.kappdev.worktracker.tracker_feature.domain.model.getDurationInSecond
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

class GetDailyReportFor @Inject constructor(
    @Named("appActivityRepository") private val activityRepository: ActivityRepository,
    @Named("appSessionRepository") private val sessionRepository: SessionRepository
) {

    operator fun invoke(date: LocalDate): List<ReportData> {
        val data = mutableListOf<ReportData>()
        val dataMap = mutableMapOf<Long, Long>()

        val sessions = sessionRepository.getSessionForDate(date)
        sessions.forEach { session ->
            val id = session.activityId
            dataMap[id] = (dataMap[id] ?: 0) + session.getDurationInSecond()
        }

        val totalSum = dataMap.values.sum()
        dataMap.forEach { entry ->
            activityRepository.getActivityById(entry.key)?.let { activity ->
                val percent = entry.value.toFloat() / totalSum
                val value = ReportData(
                    activity = activity,
                    timeValue = entry.value,
                    percent = percent
                )

                data.add(value)
            }
        }

        return data.sortedByDescending { it.timeValue }
    }
}