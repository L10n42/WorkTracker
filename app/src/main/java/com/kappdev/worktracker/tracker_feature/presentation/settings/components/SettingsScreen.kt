package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.presentation.InfoSnackbarHandler
import com.kappdev.worktracker.core.presentation.common_components.LoadingDialog
import com.kappdev.worktracker.tracker_feature.presentation.common.components.SelectorField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.TimePicker
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.settings.SettingsViewModel
import com.kappdev.worktracker.ui.spacing
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val everydayReportsEnable = viewModel.everydayReportEnable.value
    val reportTime = viewModel.reportTime.value
    val showTimeTemplates = viewModel.showTimeTemplates.value
    val focusManager = LocalFocusManager.current

    InfoSnackbarHandler(hostState = scaffoldState.snackbarHostState, snackbarState = viewModel.snackbarState)

    LoadingDialog(isVisible = viewModel.isLoading.value)

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

    val importDbLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let(viewModel::importDatabaseFrom)
        }
    )

    Scaffold(
        topBar = {
            SettingsTopBar(navigate = navController::navigate)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentPadding = PaddingValues(all = MaterialTheme.spacing.medium)
        ) {
            item {
                TitledSwitch(
                    title = stringResource(R.string.show_time_templates),
                    modifier = Modifier.fillMaxWidth(),
                    checked = showTimeTemplates,
                    onSwitch = viewModel::enableTimeTemplates
                )
            }

            settingsDivider()

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

            settingsSpace()

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

            settingsDivider()

            item {
                SettingItem(
                    titleRes = R.string.db_export_title,
                    descriptionRes = R.string.db_export_description,
                    onClick = viewModel::exportDatabase
                )
            }

            settingsDivider()

            item {
                SettingItem(
                    titleRes = R.string.db_import_title,
                    descriptionRes = R.string.db_import_description,
                    onClick = {
                        importDbLauncher.launch("application/zip")
                    }
                )
            }
        }
    }
}

private fun LazyListScope.settingsSpace() = item {
    VerticalSpace(space = 16.dp)
}

private fun LazyListScope.settingsDivider() = item {
    Column {
        VerticalSpace(space = 16.dp)
        Divider()
        VerticalSpace(space = 16.dp)
    }
}