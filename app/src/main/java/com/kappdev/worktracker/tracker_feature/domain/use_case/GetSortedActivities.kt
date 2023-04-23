package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.util.ActivityOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class GetSortedActivities @Inject constructor(
    @Named("appActivityRepository") private val repository: ActivityRepository
) {
    operator fun invoke(order: ActivityOrder): List<Activity> {
        return repository.getSorted(order)
    }
}