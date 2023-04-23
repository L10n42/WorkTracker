package com.kappdev.worktracker.tracker_feature.domain.util

import java.util.*

object DateTimeHelper {

    fun getHourOfDay(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun isCurrentDay(timestamp: Long): Boolean {
        val timestampDay = getDayFrom(timestamp)
        val currentDay = getCurrentDay()
        return timestampDay == currentDay
    }

    private fun getCurrentDay() = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    private fun getDayFrom(timestamp: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
}