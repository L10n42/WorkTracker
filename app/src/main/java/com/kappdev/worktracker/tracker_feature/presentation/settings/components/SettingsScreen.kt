package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.core.presentation.InfoSnackbarHandler
import com.kappdev.worktracker.core.presentation.common_components.LoadingDialog
import com.kappdev.worktracker.tracker_feature.presentation.common.components.SelectorField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.ServiceInfoSheet
import com.kappdev.worktracker.tracker_feature.presentation.common.components.TimePicker
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.settings.SettingsViewModel
import com.kappdev.worktracker.ui.spacing
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            confirmStateChange = { newState ->
                if (newState == BottomSheetValue.Collapsed) viewModel.showServiceInfo(false)
                true
            }
        )
    )
    val context = LocalContext.current
    val everydayReportsEnable = viewModel.everydayReportEnable.value
    val reportTime = viewModel.reportTime.value
    val showTimeTemplates = viewModel.showTimeTemplates.value
    val isThemeDark = viewModel.isThemeDark.value
    val focusManager = LocalFocusManager.current
    val showServiceInfo = viewModel.showServiceInfo.value

    LaunchedEffect(showServiceInfo) {
        if (showServiceInfo) {
            scaffoldState.bottomSheetState.expand()
        } else {
            if (scaffoldState.bottomSheetState.isExpanded) scaffoldState.bottomSheetState.collapse()
        }
    }

    InfoSnackbarHandler(hostState = scaffoldState.snackbarHostState, snackbarState = viewModel.snackbarState)

    LoadingDialog(isVisible = viewModel.isLoading.value)

    var showAlarmPermissionDialog by remember { mutableStateOf(false) }
    if (showAlarmPermissionDialog) {
        AlarmPermissionAlertDialog(
            onDismiss = { showAlarmPermissionDialog = false },
            onGrant = { context.openExactAlarmSettingPage() }
        )
    }

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

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            ServiceInfoSheet {
                viewModel.showServiceInfo(false)
            }
        },
        topBar = {
            SettingsTopBar {
                navController.navigate(Screen.Main.route)
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState,
                snackbar = { data ->
                    Snackbar(snackbarData = data)
                }
            )
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
                    title = stringResource(R.string.dark_theme),
                    modifier = Modifier.fillMaxWidth(),
                    checked = isThemeDark,
                    onSwitch = viewModel::setDarkTheme
                )
            }

            settingsDivider(space = 8.dp)

            item {
                TitledSwitch(
                    title = stringResource(R.string.show_time_templates),
                    modifier = Modifier.fillMaxWidth(),
                    checked = showTimeTemplates,
                    onSwitch = viewModel::enableTimeTemplates
                )
            }

            settingsDivider(space = 8.dp)

            item {
                TitledSwitch(
                    title = stringResource(R.string.everyday_reports_enable_title),
                    modifier = Modifier.fillMaxWidth(),
                    checked = everydayReportsEnable,
                    onSwitch = onSwitch@ { enable ->
                        if (enable && viewModel.needAlarmPermission()) {
                            showAlarmPermissionDialog = true
                        } else {
                            viewModel.setEverydayReportsEnable(enable)
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

            settingsDivider(space = 8.dp)

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

            settingsDivider()

            item {
                SettingItem(
                    titleRes = R.string.service_issue_setting,
                    onClick = {
                        viewModel.showServiceInfo(true)
                    }
                )
            }
        }
    }
}

private fun Context.openExactAlarmSettingPage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        startActivity(
            Intent(
                ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                Uri.parse("package:$packageName")
            )
        )
    }
}

private fun LazyListScope.settingsSpace() = item {
    VerticalSpace(space = 16.dp)
}

private fun LazyListScope.settingsDivider(space: Dp = 0.dp) = item {
    Column {
        VerticalSpace(space)
        Divider()
        VerticalSpace(space)
    }
}