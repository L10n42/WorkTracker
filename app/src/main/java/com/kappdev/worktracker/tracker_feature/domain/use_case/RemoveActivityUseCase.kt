package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Named

@ViewModelScoped
class RemoveActivityUseCase @Inject constructor(
    @Named("appActivityRepository") private val activityRepository: ActivityRepository,
    @Named("appSessionRepository") private val sessionRepository: SessionRepository
) {

    operator fun invoke(id: Long) {
        activityRepository.deleteActivityById(id)
        sessionRepository.deleteSessionsByActivity(id)
    }

    operator fun invoke(activity: Activity) = invoke(activity.id)
}