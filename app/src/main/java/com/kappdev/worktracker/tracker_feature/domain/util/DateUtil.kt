package com.kappdev.worktracker.tracker_feature.domain.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object DateUtil {

    fun getDateOf(timestamp: Long): LocalDate {
        val instant = Instant.ofEpochMilli(timestamp)
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun getMonthDays(date: LocalDate): List<Int> {
        val start = date.withDayOfMonth(1)
        val daysInMonth = date.month.length(date.isLeapYear)
        return (0 until daysInMonth).map { start.plusDays(it.toLong()).dayOfMonth }
    }

}