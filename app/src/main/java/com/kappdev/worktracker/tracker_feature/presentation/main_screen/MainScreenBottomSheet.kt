package com.kappdev.worktracker.tracker_feature.presentation.main_screen

sealed class MainScreenBottomSheet {
    data class TimePicker(val activityId: Long, val activityName: String): MainScreenBottomSheet()
    object Sort: MainScreenBottomSheet()
    object ServiceInfo: MainScreenBottomSheet()
}
