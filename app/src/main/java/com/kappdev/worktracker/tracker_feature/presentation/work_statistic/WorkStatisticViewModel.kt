package com.kappdev.worktracker.tracker_feature.presentation.work_statistic

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kappdev.worktracker.tracker_feature.domain.model.PieChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkStatisticViewModel @Inject constructor(

) : ViewModel() {

    private val _data = mutableStateOf<List<PieChartData>>(emptyList())
    val data: State<List<PieChartData>> = _data




}