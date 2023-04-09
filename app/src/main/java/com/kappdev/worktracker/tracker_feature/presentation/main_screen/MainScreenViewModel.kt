package com.kappdev.worktracker.tracker_feature.presentation.main_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_kappdev_worktracker_MainActivity_GeneratedInjector
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getActivities: GetActivitiesUseCase
) : ViewModel() {

    private val _activities = mutableStateOf<List<Activity>>(emptyList())
    val activities: State<List<Activity>> = _activities

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    private var activityJob: Job? = null

    init {
        getAllActivities()
    }

    private fun getAllActivities() {
        activityJob?.cancel()
        activityJob = getActivities().onEach { activities ->
            _activities.value = activities
        }.launchIn(viewModelScope)
    }

    fun navigate(route: String) {
        _navigate.value = route
    }

    fun clearNavigationRoute() {
        _navigate.value = null
    }
}