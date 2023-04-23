package com.kappdev.worktracker.tracker_feature.domain.repository

import com.kappdev.worktracker.tracker_feature.domain.model.Session

interface StatisticRepository {

    fun getDailySessionsFor(activityId: Long): List<Session>

}