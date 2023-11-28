package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kappdev.worktracker.R

@Composable
fun AlarmPermissionAlertDialog(
    onDismiss: () -> Unit,
    onGrant: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.permission_required_title))
        },
        text = {
            Text(stringResource(R.string.alarm_permission_request_message))
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onGrant()
                },
                shape = CircleShape
            ) {
                Text(stringResource(R.string.btn_grant))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = CircleShape,
            ) {
                Text(stringResource(R.string.btn_deny))
            }
        }
    )
}