package com.kappdev.worktracker.tracker_feature.presentation.common.components.timer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TimerButton(
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .size(56.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground,
                shape = CircleShape
            ),
        onClick = onClick
    ) {
        Icon(
            tint = color,
            imageVector = icon,
            modifier = Modifier.size(32.dp),
            contentDescription = "icon for the Timer Button"
        )
    }
}