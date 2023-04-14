package com.kappdev.worktracker.tracker_feature.presentation.common.components.timer_bar

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState

@Composable
fun StopResumeButton(
    modifier: Modifier = Modifier,
    state: ServiceState,
    onStop: () -> Unit,
    onResume: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = {
            when {
                (state == ServiceState.Started) -> onStop()
                (state == ServiceState.Stopped) -> onResume()
            }
        }
    ) {
        Icon(
            imageVector = if (state == ServiceState.Started) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            contentDescription = "stop resume button fro bottom bar",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(32.dp)
        )
    }
}