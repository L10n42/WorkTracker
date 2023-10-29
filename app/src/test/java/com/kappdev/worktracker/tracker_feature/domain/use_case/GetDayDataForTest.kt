package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.nhaarman.mockitokotlin2.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class GetDayDataForTest {

    private lateinit var getDayDataFor: GetDayDataFor
    private lateinit var repository: StatisticRepository

    @Before
    fun setup() {
        repository = mock()
        getDayDataFor = GetDayDataFor(repository)
    }

    @Test
    fun `sessions time is within same hour`() {
        val activityId = 1L
        val date = LocalDate.of(2023, 1, 1)

        val session1 = Session(
            id = 1L,
            activityId = activityId,
            startTimestamp = 1672561800000, // January 1, 2023 10:30:00
            endTimestamp = 1672562700000,  // January 1, 2023 10:45:00
            timeInSec = 900
        )
        val session2 = Session(
            id = 2L,
            activityId = activityId,
            startTimestamp = 1672575300000, // January 1, 2023 14:15:00
            endTimestamp = 1672577400000, // January 1, 2023 14:50:00
            timeInSec = 2100
        )

        whenever(repository.getForDay(activityId, date)).thenReturn(listOf(session1, session2))

        val result = getDayDataFor(activityId, date)

        assertEquals(900L, result["10"])
        assertEquals(2100L, result["14"])
    }

    @Test
    fun `sessions time span across different hours`() {
        val activityId = 1L
        val date = LocalDate.of(2023, 1, 1)

        val session1 = Session(
            id = 1L,
            activityId = activityId,
            startTimestamp = 1672562700000, // January 1, 2023 10:45:00
            endTimestamp = 1672564500000, // January 1, 2023 11:15:00
            timeInSec = 1800
        )
        val session2 = Session(
            id = 2L,
            activityId = activityId,
            startTimestamp = 1672577400000, // January 1, 2023 14:50:00
            endTimestamp = 1672583400000, // January 1, 2023 16:30:00
            timeInSec = 6000L
        )

        whenever(repository.getForDay(activityId, date)).thenReturn(listOf(session1, session2))

        val result = getDayDataFor(activityId, date)

        assertEquals(900L, result["10"])
        assertEquals(900L, result["11"])
        assertEquals(600L, result["14"])
        assertEquals(3600L, result["15"])
        assertEquals(1800L, result["16"])
    }

}