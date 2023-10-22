package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.common.components.SelectorField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.TimePicker
import com.kappdev.worktracker.tracker_feature.presentation.settings.SettingsViewModel
import com.kappdev.worktracker.ui.spacing
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val everydayReportsEnable = viewModel.everydayReportEnable.value
    val reportTime = viewModel.reportTime.value
    val focusManager = LocalFocusManager.current

    val timeDialogState = rememberMaterialDialogState()
    TimePicker(
        initTime = reportTime,
        state = timeDialogState,
        label = stringResource(R.string.select_report_time_title),
        closePicker = {
            timeDialogState.hide()
            focusManager.clearFocus()
        },
        setTime = { selectedTime ->
            focusManager.clearFocus()
            viewModel.setReportTime(selectedTime)
            viewModel.updateRemainder()
        }
    )

    Scaffold(
        topBar = {
            SettingsTopBar(navigate = navController::navigate)
        }
    ) { scaffoldPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentPadding = PaddingValues(all = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item {
                TitledSwitch(
                    title = stringResource(R.string.everyday_reports_enable_title),
                    modifier = Modifier.fillMaxWidth(),
                    checked = everydayReportsEnable,
                    onSwitch = { enable ->
                        viewModel.setEverydayReportsEnable(enable)
                        if (enable) {
                            viewModel.updateRemainder()
                        } else {
                            viewModel.cancelRemainder()
                        }
                    }
                )
            }

            item {
                SelectorField(
                    value = reportTime.toString(),
                    enable = everydayReportsEnable,
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.label_report_time),
                    onClick = {
                        timeDialogState.show()
                    }
                )
            }
        }
    }
}