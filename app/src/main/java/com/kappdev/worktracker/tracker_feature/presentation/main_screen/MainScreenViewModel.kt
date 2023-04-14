package com.kappdev.worktracker.tracker_feature.presentation.main_screen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getActivities: GetActivitiesUseCase,
    private val app: Application,
    val stopwatchController: StopwatchController,
    val countdownController: CountdownController
) : ViewModel() {

    private val _activities = mutableStateOf<List<Activity>>(emptyList())
    val activities: State<List<Activity>> = _activities

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    private val _bottomSheet = mutableStateOf<MainScreenBottomSheet?>(null)
    val bottomSheet: State<MainScreenBottomSheet?> = _bottomSheet

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

    fun clearSheet() = closeSheet()

    fun closeSheet() {
        _bottomSheet.value = null
    }

    fun openSheet(sheet: MainScreenBottomSheet) {
        _bottomSheet.value = sheet
    }

    fun navigate(route: String) {
        _navigate.value = route
    }

    fun clearNavigationRoute() {
        _navigate.value = null
    }
}