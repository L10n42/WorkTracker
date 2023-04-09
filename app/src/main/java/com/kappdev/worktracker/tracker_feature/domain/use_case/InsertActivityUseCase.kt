package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import javax.inject.Inject
import javax.inject.Named

class InsertActivityUseCase @Inject constructor(
    @Named("appActivityRepository") private val repository: ActivityRepository
) {

    suspend operator fun invoke(activity: Activity) {
        repository.insertActivity(activity)
    }
}