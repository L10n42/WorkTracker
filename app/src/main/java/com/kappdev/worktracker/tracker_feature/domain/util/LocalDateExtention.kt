package com.kappdev.worktracker.tracker_feature.domain.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields

fun LocalDate.getWeek(): Pair<LocalDate, LocalDate> {
    val weekFields: TemporalField = WeekFields.of(DayOfWeek.MONDAY, 7).dayOfWeek()
    val startOfWeek = this.with(weekFields, 1)
    val endOfWeek = this.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))

    return Pair(startOfWeek, endOfWeek)
}