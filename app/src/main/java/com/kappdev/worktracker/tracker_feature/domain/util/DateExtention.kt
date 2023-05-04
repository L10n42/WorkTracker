package com.kappdev.worktracker.tracker_feature.domain.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

fun Pair<LocalDate, LocalDate>.emptyPeriodMap(): MutableMap<LocalDate, Long> {
    val map = mutableMapOf<LocalDate, Long>()
    var currentDate = this.first

    while (currentDate <= this.second) {
        map[currentDate] = 0
        currentDate = currentDate.plusDays(1)
    }

    return map
}

fun LocalDate.getMonthToDisplay(): String {
    val currentYear = LocalDate.now().year
    val month = this.month.getName()
    val year = this.year

    val needYear = (year != currentYear)
    return "$month ${if (needYear) ", $year" else ""}"
}

fun LocalDate.getWeekToDisplay(): String {
    val week = this.getWeek()
    val firstMonth = week.first.month.getName()
    val secondMonth = week.second.month.getName()

    val firstDay = week.first.dayOfMonth
    val secondDay = week.second.dayOfMonth

    val firstYear = week.first.year
    val secondYear = week.second.year
    val currentYear = LocalDate.now().year

    val needYear = (firstYear != currentYear) || (secondYear != currentYear)
    val firstDate = "$firstDay $firstMonth${if (needYear) ", $firstYear" else ""}"
    val secondDate = "$secondDay $secondMonth${if (needYear) ", $secondYear" else ""}"
    return "$firstDate - $secondDate"
}

fun LocalDate.getMonthPeriod(): Pair<LocalDate, LocalDate> {
    val startOfMonth = this.with(TemporalAdjusters.firstDayOfMonth())
    val endOfMonth = this.with(TemporalAdjusters.lastDayOfMonth())

    return Pair(startOfMonth, endOfMonth)
}

fun LocalDate.getWeek(): Pair<LocalDate, LocalDate> {
    val weekFields: TemporalField = WeekFields.of(DayOfWeek.MONDAY, 7).dayOfWeek()
    val startOfWeek = this.with(weekFields, 1)
    val endOfWeek = this.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))

    return Pair(startOfWeek, endOfWeek)
}

fun Month.getName(): String {
    return this.name.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}