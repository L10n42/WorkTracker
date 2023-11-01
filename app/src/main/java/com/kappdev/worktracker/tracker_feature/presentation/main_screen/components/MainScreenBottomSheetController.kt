package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.runtime.Composable
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.model.inMillis
import com.kappdev.worktracker.tracker_feature.presentation.common.components.ServiceInfoSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel

@Composable
fun MainScreenBottomSheetController(
    sheet: MainScreenBottomSheet,
    viewModel: MainScreenViewModel,
    countdownState: ServiceState,
    closeSheet: () -> Unit
) {
    when (sheet) {
        is MainScreenBottomSheet.TimePicker -> {
            SelectTimeBottomSheet(
                closeSheet = closeSheet,
                showTimeTemplateByDefault = viewModel.showTimeTemplateByDefault
            ) { time ->
                if (countdownState == ServiceState.Idle) {
                    viewModel.countdownController.start(
                        activityId = sheet.activityId,
                        activityName = sheet.activityName,
                        durationInMillis = time.inMillis()
                    )
                }
            }
        }
        is MainScreenBottomSheet.Sort -> {
            ActivitiesOrderSheet(viewModel)
        }
        is MainScreenBottomSheet.ServiceInfo -> ServiceInfoSheet {
            viewModel.viewServiceInfo()
            closeSheet()
        }
    }
}