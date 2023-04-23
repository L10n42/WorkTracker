package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import javax.inject.Inject
import javax.inject.Named

class GetActivityByIdUseCase @Inject constructor(
    @Named("appActivityRepository") private val repository: ActivityRepository
) {

    operator fun invoke(id: Long): Activity? {
        return repository.getActivityById(id)
    }
}