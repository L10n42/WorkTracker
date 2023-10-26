package com.kappdev.worktracker.tracker_feature.domain.use_case

import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Named

@ViewModelScoped
class InsertActivityUseCase @Inject constructor(
    @Named("appActivityRepository") private val repository: ActivityRepository
) {

    suspend operator fun invoke(activity: Activity) {
        repository.insertActivity(activity)
    }
}