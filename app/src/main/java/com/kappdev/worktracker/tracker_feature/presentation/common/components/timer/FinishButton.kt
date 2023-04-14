package com.kappdev.worktracker.tracker_feature.presentation.common.components.timer

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kappdev.worktracker.ui.theme.StopRed

@Composable
fun FinishButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Rounded.Stop,
            contentDescription = "finish timer button for bottom bar",
            tint = StopRed
        )
    }
}