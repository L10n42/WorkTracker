package com.kappdev.worktracker.tracker_feature.presentation.work_statistic

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.ReportData
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetDailyReportFor
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class WorkStatisticViewModel @Inject constructor(
    @Named("AppReport") private val getDailyReportFor: GetDailyReportFor
) : ViewModel() {

    private val _data = mutableStateOf<List<ReportData>>(emptyList())
    val data: State<List<ReportData>> = _data

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
            _data.value = getDailyReportFor(date.value)
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