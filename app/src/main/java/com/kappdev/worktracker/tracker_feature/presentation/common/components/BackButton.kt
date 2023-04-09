package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.core.navigation.Screen

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "back button",
            tint = MaterialTheme.colors.onSurface
        )
    }
}