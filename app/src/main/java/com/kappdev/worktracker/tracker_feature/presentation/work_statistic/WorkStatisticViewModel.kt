package com.kappdev.worktracker.tracker_feature.presentation.work_statistic

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.worktracker.tracker_feature.domain.model.PieChartData
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetPieChartData
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkStatisticViewModel @Inject constructor(
    private val getPieChartData: GetPieChartData
) : ViewModel() {

    private val _data = mutableStateOf<List<PieChartData>>(emptyList())
    val data: State<List<PieChartData>> = _data

    private val _dataState = mutableStateOf(DataState.IDLE)
    val dataState: State<DataState> = _dataState

    private var dataJob: Job? = null

    fun getData() {
        dataJob?.cancel()
        dataJob = viewModelScope.launch(Dispatchers.IO) {
            _dataState.value = DataState.LOADING
            _data.value = getPieChartData()
            _dataState.value = if (data.value.isEmpty()) DataState.NO_DATA else DataState.READY
        }
    }




}