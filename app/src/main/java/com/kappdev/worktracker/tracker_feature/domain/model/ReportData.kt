package com.kappdev.worktracker.tracker_feature.domain.model

data class ReportData(
    val activity: Activity,
    val timeValue: Long,
    val percent: Float
)