package com.kappdev.worktracker.tracker_feature.domain.util

import org.junit.Assert.*
import org.junit.Test

class DateTimeHelperTest {

    @Test
    fun `get hour of day test`() {
        val timestamp = 1672561800000L // January 1, 2023 10:30:00
        val result = DateTimeHelper.getHourOfDay(timestamp)
        assertEquals(10, result)
    }

    @Test
    fun `get minutes of hour test`() {
        val timestamp = 1672561800000L // January 1, 2023 10:30:00
        val result = DateTimeHelper.getMinuteOfHour(timestamp)
        assertEquals(30, result)
    }

}