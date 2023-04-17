package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.common.components.ConfirmationDialog
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenDialog
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel

@Composable
fun MainScreenDialogController(
    dialog: MainScreenDialog?,
    viewModel: MainScreenViewModel
) {
    when (dialog) {
        is MainScreenDialog.RemoveActivitiesConf -> {
            ConfirmationDialog(
                title = stringResource(R.string.remove_title),
                message = stringResource(R.string.remove_activities_msg),
                confirmText = stringResource(R.string.btn_remove),
                closeDialog = viewModel::closeDialog,
                onConfirm = {
                    viewModel.removeSelected {
                        viewModel.cancelSelection()
                    }
                }
            )
        }
        else -> {}
    }
}