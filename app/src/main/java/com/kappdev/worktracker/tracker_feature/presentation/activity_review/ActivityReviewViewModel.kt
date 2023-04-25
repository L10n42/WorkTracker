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
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ActivityReviewViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val statisticRepository: StatisticRepository
) : ViewModel() {
    private var dailySessions = emptyList<Session>()

    private val _totalDailyWorkingTime = mutableStateOf("")
    val totalDailyWorkingTime: State<String> = _totalDailyWorkingTime

    private val _dailyGraphData = mutableStateOf<Map<Int, Long>>(emptyMap())
    val dailyGraphData: State<Map<Int, Long>> = _dailyGraphData

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private val _graphDataState = mutableStateOf(GraphDataState.IDLE)
    val graphDataState: State<GraphDataState> = _graphDataState

    private val _graphDate = mutableStateOf<LocalDate>(LocalDate.now())
    val graphDate: State<LocalDate> = _graphDate

    private val _currentActivity = mutableStateOf<Activity?>(null)
    val currentActivity: State<Activity?> = _currentActivity

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    private var dailyGraphJob: Job? = null

    fun getDataFor(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setDataState(DataState.LOADING)
            _currentActivity.value = getActivityById(id)

            dailySessions = statisticRepository.getDailySessionsFor(id)
            _dailyGraphData.value = MakeDailyGraphData().invoke(dailySessions)
            countTotalDailyWork()

            if (currentActivity.value == null) setDataState(DataState.NO_DATA) else setDataState(DataState.READY)
        }
    }

    private fun getDailyGraphData() {
        currentActivity.value?.let { activity ->

            dailyGraphJob?.cancel()
            dailyGraphJob = viewModelScope.launch(Dispatchers.IO) {
                _graphDataState.value = GraphDataState.LOADING

                dailySessions = statisticRepository.getDailySessionsFor(activity.id, graphDate.value)
                _dailyGraphData.value = MakeDailyGraphData().invoke(dailySessions)
                countTotalDailyWork()

                _graphDataState.value = GraphDataState.READY
            }
        }
    }

    private fun countTotalDailyWork() {
        var totalTimeInSec = 0L
        dailySessions.forEach { session ->
            totalTimeInSec += session.timeInSec
        }

        _totalDailyWorkingTime.value = if (totalTimeInSec > 0L) TimeUtil.toTimeFormat(totalTimeInSec) else "0 sec"
    }

    private fun setDataState(state: DataState) { _dataState.value = state }

    fun setDateAndUpdate(date: LocalDate) {
        _graphDate.value = date
        getDailyGraphData()
    }
    fun setDate(date: LocalDate) { _graphDate.value = date }

    fun gotoEdit() {
        val id = currentActivity.value?.id
        id?.let {
            navigate(Screen.AddEditActivity.route + "?activityId=$it")
        }
    }
    fun navigate(route: String) { _navigate.value = route }
    fun clearNavigationRoute() { _navigate.value = null }
}