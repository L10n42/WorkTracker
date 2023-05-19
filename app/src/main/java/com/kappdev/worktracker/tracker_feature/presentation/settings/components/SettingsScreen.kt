package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val voiceNotificationEnable = viewModel.voiceNotification.value
    val everydayReportsEnable = viewModel.everydayReportEnable.value
    val notificationMsg = viewModel.notificationMsg.value
    val privacyEnable = viewModel.privacyEnable.value
    val reportTime = viewModel.reportTime.value
    val password = viewModel.password.value
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

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

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    fun closeSheet() = scope.launch { sheetState.hide() }
    fun openSheet() = scope.launch { sheetState.show() }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            SetPasswordBottomSheet(
                password = password,
                onPasswordChanged = viewModel::setPassword,
                onCommit = {
                    val isCorrect = viewModel.isPasswordCorrect()
                    if (isCorrect) {
                        viewModel.setPrivacyEnable(true)
                        viewModel.updatePassword()
                        closeSheet()
                        focusManager.clearFocus()
                    }
                    isCorrect
                }
            )
        }
    ) {
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
                        title = stringResource(R.string.voice_notification_title),
                        modifier = Modifier.fillMaxWidth(),
                        checked = voiceNotificationEnable,
                        onSwitch = viewModel::setVoiceNotification
                    )
                }

                item {
                    NotificationMsgField(
                        value = notificationMsg,
                        enable = voiceNotificationEnable,
                        label = stringResource(R.string.label_notification_msg),
                        hint = stringResource(R.string.notification_msg_placeholder),
                        modifier = Modifier.fillMaxWidth(),
                        onValueChanged = viewModel::setNotificationMsg
                    )
                }

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

                item {
                    TitledSwitch(
                        title = stringResource(R.string.privacy_enable_title),
                        modifier = Modifier.fillMaxWidth(),
                        checked = privacyEnable,
                        onSwitch = { enable ->
                            if (enable) {
                                openSheet()
                            } else {
                                viewModel.setPrivacyEnable(false)
                                viewModel.updatePassword()
                            }
                        }
                    )
                }
            }
        }
    }
}