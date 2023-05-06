package com.kappdev.worktracker.tracker_feature.domain.model

import androidx.compose.ui.graphics.Color

data class PieChartData(
    val activity: Activity,
    val timeValue: Long,
    val percent: Float,
    val color: Color
)