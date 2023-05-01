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
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetCalendarDataFor
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetWeekDataFor
import com.kappdev.worktracker.tracker_feature.domain.use_case.MakeDailyGraphData
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ActivityReviewViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val statisticRepository: StatisticRepository,
    private val getCalendarDataFor: GetCalendarDataFor,
    private val getWeekDataFor: GetWeekDataFor
) : ViewModel() {
    private var dailySessions = emptyList<Session>()

    private val _totalDailyWorkingTime = mutableStateOf("")
    val totalDailyWorkingTime: State<String> = _totalDailyWorkingTime

    private val _graphViewState = mutableStateOf(GraphViewState.DAY)
    val graphViewState: State<GraphViewState> = _graphViewState

    private val _graphData = mutableStateOf<Map<String, Long>>(emptyMap())
    val graphData: State<Map<String, Long>> = _graphData

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private val _graphDataState = mutableStateOf(GraphDataState.IDLE)
    val graphDataState: State<GraphDataState> = _graphDataState

    private val _graphDate = mutableStateOf<LocalDate>(LocalDate.now())
    val graphDate: State<LocalDate> = _graphDate

    private val _calendarDate = mutableStateOf<LocalDate>(LocalDate.now())
    val calendarDate: State<LocalDate> = _calendarDate

    private val _calendarData = mutableStateOf<Map<Int, Long>>(emptyMap())
    val calendarData: State<Map<Int, Long>> = _calendarData

    private val _currentActivity = mutableStateOf<Activity?>(null)
    val currentActivity: State<Activity?> = _currentActivity

    private val _navigate = mutableStateOf<String?>(null)
    val navigate: State<String?> = _navigate

    private var graphDataJob: Job? = null
    private var calendarDataJob: Job? = null

    fun getDataFor(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setDataState(DataState.LOADING)
            _currentActivity.value = getActivityById(id)

            updateGraphData()
            countTotalDailyWork()
            updateCalendarData()

            if (currentActivity.value == null) setDataState(DataState.NO_DATA) else setDataState(DataState.READY)
        }
    }

    private fun updateCalendarData() {
        currentActivity.value?.let { activity ->
            getCalendarData(activity.id, calendarDate.value)
        }
    }

    private fun getCalendarData(activityId: Long, date: LocalDate) {
        calendarDataJob?.cancel()
        calendarDataJob = viewModelScope.launch(Dispatchers.IO) {
            _calendarData.value = getCalendarDataFor(activityId, date)
        }
    }

    fun updateGraphData() {
        currentActivity.value?.id?.let { activityId ->
            when (graphViewState.value) {
                GraphViewState.DAY -> getDailyGraphData(activityId)
                GraphViewState.WEEK -> getWeekData(activityId, graphDate.value)
                GraphViewState.MONTH -> {}
                GraphViewState.YEAR -> {}
            }
        }
    }

    private fun getWeekData(activityId: Long, date: LocalDate) {
        graphDataJob?.cancel()
        graphDataJob = viewModelScope.launch(Dispatchers.IO) {
            _graphData.value = getWeekDataFor(activityId, date)
        }
    }

    private fun getDailyGraphData(activityId: Long) {
        graphDataJob?.cancel()
        graphDataJob = viewModelScope.launch(Dispatchers.IO) {
            _graphDataState.value = GraphDataState.LOADING

            dailySessions = statisticRepository.getDailySessionsFor(activityId, graphDate.value)
            _graphData.value = MakeDailyGraphData().invoke(dailySessions)
            countTotalDailyWork()

            _graphDataState.value = GraphDataState.READY
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

    fun setGraphDate(date: LocalDate) { _graphDate.value = date }

    fun setGraphViewState(state: GraphViewState) { _graphViewState.value = state }

    fun updateCalendarWith(date: LocalDate) {
        _calendarDate.value = date
        updateCalendarData()
    }

    fun gotoEdit() {
        currentActivity.value?.id?.let {
            navigate(Screen.AddEditActivity.route + "?activityId=$it")
        }
    }
    fun navigate(route: String) { _navigate.value = route }
    fun clearNavigationRoute() { _navigate.value = null }
}