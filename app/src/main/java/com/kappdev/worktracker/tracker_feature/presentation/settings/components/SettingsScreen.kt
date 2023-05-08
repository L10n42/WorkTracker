package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.settings.SettingsViewModel
import com.kappdev.worktracker.ui.spacing

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val voiceNotificationEnable = viewModel.voiceNotification.value
    val notificationMsg = viewModel.notificationMsg.value

    Scaffold(
        topBar = {
            SettingsTopBar { route ->
                navController.navigate(route)
            }
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
        }
    }
}