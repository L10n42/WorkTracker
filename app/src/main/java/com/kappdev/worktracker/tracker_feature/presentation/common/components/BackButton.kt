package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BackButton(
    icon: ImageVector = Icons.Default.ArrowBack,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "back button",
            tint = MaterialTheme.colors.onSurface
        )
    }
}