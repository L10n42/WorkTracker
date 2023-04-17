package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    cancelText: String = stringResource(R.string.btn_cancel),
    confirmText: String = stringResource(R.string.btn_ok),
    closeDialog: () -> Unit,
    onCancel: () -> Unit = closeDialog,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = closeDialog,
        shape = MaterialTheme.customShape.medium,
        backgroundColor = MaterialTheme.colors.surface,
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
        },
        text = {
            Text(
                text = message,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        },
        confirmButton = {
            Button(confirmText) {
                closeDialog()
                onConfirm()
            }
        },
        dismissButton = {
            Button(cancelText, onClick = onCancel)
        }
    )
}

@Composable
private fun Button(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .padding(end = MaterialTheme.spacing.small, bottom = MaterialTheme.spacing.small)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colors.primary
        )
    }
}