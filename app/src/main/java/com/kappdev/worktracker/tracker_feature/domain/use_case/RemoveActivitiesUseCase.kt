package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import javax.inject.Inject

class RemoveActivitiesUseCase @Inject constructor(
    private val removeActivity: RemoveActivityUseCase
) {

    @JvmName("RemoveActivitiesByTheIds")
    operator fun invoke(ids: Collection<Long>) {
        ids.forEach { id ->
            removeActivity(id)
        }
    }

    @JvmName("RemoveActivities")
    operator fun invoke(activities: Collection<Activity>) {
        activities.forEach { activity ->
            removeActivity(activity)
        }
    }
}