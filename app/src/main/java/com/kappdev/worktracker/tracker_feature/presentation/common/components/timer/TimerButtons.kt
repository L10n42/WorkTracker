package com.kappdev.worktracker.tracker_feature.presentation.common.components.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.ui.theme.StopRed

@Composable
fun TimerButtons(
    modifier: Modifier = Modifier,
    state: ServiceState,
    onFinish: () -> Unit,
    onStop: () -> Unit,
    onResume: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TimerButton(
            icon = Icons.Rounded.Stop,
            color = StopRed,
            onClick = onFinish
        )

        TimerButton(
            icon = if (state == ServiceState.Started) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
            color = MaterialTheme.colors.primary
        ) {
            when {
                (state == ServiceState.Started) -> onStop()
                (state == ServiceState.Stopped) -> onResume()
            }
        }
    }
}