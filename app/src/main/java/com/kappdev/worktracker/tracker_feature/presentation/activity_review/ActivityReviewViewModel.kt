package com.kappdev.worktracker.tracker_feature.presentation.activity_review

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import com.kappdev.worktracker.tracker_feature.domain.repository.StatisticRepository
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetActivityByIdUseCase
import com.kappdev.worktracker.tracker_feature.domain.use_case.MakeDailyGraphData
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityReviewViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val statisticRepository: StatisticRepository
) : ViewModel() {
    private var dailySessions = emptyList<Session>()

    var totalDailyWorkingTime = ""
    private set
    var dailyGraphData = emptyMap<Int, Long>()
    private set

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private val _currentActivity = mutableStateOf<Activity?>(null)
    val currentActivity: State<Activity?> = _currentActivity

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    fun getDataFor(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setDataState(DataState.LOADING)
            _currentActivity.value = getActivityById(id)

            dailySessions = statisticRepository.getDailySessionsFor(id)
            dailyGraphData = MakeDailyGraphData().invoke(dailySessions)
            countTotalDailyWork()

            if (currentActivity.value == null) setDataState(DataState.NO_DATA) else setDataState(DataState.READY)
        }
    }

    private fun countTotalDailyWork() {
        var totalTimeInSec = 0L
        dailySessions.forEach { session ->
            totalTimeInSec += session.timeInSec
        }

        totalDailyWorkingTime = if (totalTimeInSec > 0L) separateTime(totalTimeInSec) else "0 sec"
    }

    private fun separateTime(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val remainingSeconds = timeInSeconds % 3600

        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return buildString {
            if (hours == 1L) append("$hours hour")
            if (hours > 1) append("$hours hours")
            if (minutes > 0) append(" $minutes min")
            if (seconds > 0) append(" $seconds sec")
        }
    }

    private fun setDataState(state: DataState) { _dataState.value = state }

    fun gotoEdit() {
        val id = currentActivity.value?.id
        id?.let {
            navigate(Screen.AddEditActivity.route + "?activityId=$it")
        }
    }
    fun navigate(route: String) { _navigate.value = route }
    fun clearNavigationRoute() { _navigate.value = null }
}