package com.kappdev.worktracker.tracker_feature.presentation.activity_review

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.use_case.*
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ActivityReviewViewModel @Inject constructor(
    private val getActivityById: GetActivityByIdUseCase,
    private val getCalendarDataFor: GetCalendarDataFor,
    private val getWeekDataFor: GetWeekDataFor,
    private val getMonthDataFor: GetMonthDataFor,
    private val getYearDataFor: GetYearDataFor,
    private val getDayDataFor: GetDayDataFor
) : ViewModel() {

    private val _totalTime = mutableStateOf("")
    val totalTime: State<String> = _totalTime

    private val _graphViewState = mutableStateOf(GraphViewState.DAY)
    val graphViewState: State<GraphViewState> = _graphViewState

    private val _graphData = mutableStateOf<Map<String, Long>>(emptyMap())
    val graphData: State<Map<String, Long>> = _graphData

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
            _currentActivity.value = getActivityById(id)
            updateGraphData()
            updateCalendarData()
        }
    }

    private fun updateCalendarData() = withActivityId { id ->
        calendarDataJob?.cancel()
        calendarDataJob = viewModelScope.launch(Dispatchers.IO) {
            _calendarData.value = getCalendarDataFor(id, calendarDate.value)
        }
    }

    fun updateGraphData() = withActivityId { activityId ->
        graphDataJob?.cancel()
        graphDataJob = viewModelScope.launch(Dispatchers.IO) {
            setGraphDataState(GraphDataState.LOADING)

            _graphData.value = when (graphViewState.value) {
                GraphViewState.DAY -> getDayDataFor(activityId, graphDate.value)
                GraphViewState.WEEK -> getWeekDataFor(activityId, graphDate.value)
                GraphViewState.MONTH -> getMonthDataFor(activityId, graphDate.value)
                GraphViewState.YEAR -> getYearDataFor(activityId, graphDate.value)
            }
            countTotalTime()
            setGraphDataState(GraphDataState.READY)
        }
    }

    private fun countTotalTime() {
        var totalTimeInSec = 0L
        graphData.value.values.forEach { totalTimeInSec += it }

        setTotalTime(
            if (totalTimeInSec > 0L) TimeUtil.splitTime(totalTimeInSec) else "0 sec"
        )
    }

    fun updateGraphWith(date: LocalDate) {
        setGraphDate(date)
        updateGraphData()
    }

    fun resetGraphDate() = setGraphDate(LocalDate.now())

    fun setGraphViewState(state: GraphViewState) { _graphViewState.value = state }

    fun updateCalendarWith(date: LocalDate) {
        _calendarDate.value = date
        updateCalendarData()
    }

    fun gotoEdit() = withActivityId { id ->
        navigate(Screen.AddEditActivity.route + "?activityId=$id")
    }

    fun navigate(route: String) { _navigate.value = route }
    fun clearNavigationRoute() { _navigate.value = null }

    private fun setTotalTime(value: String) { _totalTime.value = value }

    private fun setGraphDate(date: LocalDate) { _graphDate.value = date }
    private fun setGraphDataState(state: GraphDataState) { _graphDataState.value = state }

    private fun withActivityId(block: (id: Long) -> Unit) {
        currentActivity.value?.id?.let { block(it) }
    }
}