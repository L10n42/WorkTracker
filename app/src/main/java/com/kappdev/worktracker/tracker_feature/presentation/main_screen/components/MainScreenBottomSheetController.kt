package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.runtime.Composable
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel

@Composable
fun MainScreenBottomSheetController(
    sheet: MainScreenBottomSheet,
    viewModel: MainScreenViewModel,
    countdownState: ServiceState
) {
    when (sheet) {
        is MainScreenBottomSheet.TimePicker -> {
            SetTimerBottomSheet(viewModel, countdownState, sheet)
        }
        is MainScreenBottomSheet.Sort -> {
            ActivitiesOrderSheet(viewModel)
        }
    }
}