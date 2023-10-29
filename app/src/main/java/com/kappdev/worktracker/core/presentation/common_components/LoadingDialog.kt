package com.kappdev.worktracker.core.presentation.common_components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialog(
    isVisible: Boolean
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            properties = DialogProperties(
                dismissOnBackPress = false, dismissOnClickOutside = false
            )
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.surface
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}