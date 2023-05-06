package com.kappdev.worktracker.tracker_feature.presentation.work_statistic

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.PieChartData
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetPieChartDataFor
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorkStatisticViewModel @Inject constructor(
    private val getPieChartDataFor: GetPieChartDataFor
) : ViewModel() {

    private val _data = mutableStateOf<List<PieChartData>>(emptyList())
    val data: State<List<PieChartData>> = _data

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private val _date = mutableStateOf<LocalDate>(LocalDate.now())
    val date: State<LocalDate> = _date

    private val _totalTime = mutableStateOf(0L)
    val totalTime: State<Long> = _totalTime

    private var dataJob: Job? = null

    fun getData() {
        dataJob?.cancel()
        dataJob = viewModelScope.launch(Dispatchers.IO) {
            _dataState.value = DataState.LOADING
            _data.value = getPieChartDataFor(date.value)
            setTotalTime(countTotalTime())
            _dataState.value = if (data.value.isEmpty()) DataState.NO_DATA else DataState.READY
        }
    }

    private fun countTotalTime(): Long {
        var totalTime = 0L
        data.value.forEach { pieData ->
            totalTime += pieData.timeValue
        }
        return totalTime
    }

    private fun setTotalTime(time: Long) {
        _totalTime.value = time
    }

    fun setDate(date: LocalDate) {
        _date.value = date
    }
}