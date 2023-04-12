package com.kappdev.worktracker.tracker_feature.presentation.stopwatch_timer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.data.service.StopwatchService
import com.kappdev.worktracker.tracker_feature.data.service.StopwatchState
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.ui.spacing
import com.kappdev.worktracker.ui.theme.StopRed

@Composable
fun StopwatchTimerScreen(
    navController: NavHostController,
    stopwatchService: StopwatchService,
    stopwatchController: StopwatchController
) {
    val time by stopwatchService.time
    val activityName by stopwatchService.activityName
    val stopwatchState by stopwatchService.currentState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = MaterialTheme.spacing.extraLarge * 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedTimer(
                time = time,
                style = TextStyle(
                    fontSize = 64.sp,
                    color = MaterialTheme.colors.primary
                )
            )

            Text(
                text = activityName,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(all = MaterialTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TimerButton(
                icon = Icons.Rounded.Stop,
                color = StopRed
            ) {
                stopwatchController.finish()
                navController.navigate(Screen.Main.route)
            }


            TimerButton(
                icon = if (stopwatchState == StopwatchState.Started) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                color = MaterialTheme.colors.primary
            ) {
                when {
                    (stopwatchState == StopwatchState.Started) -> stopwatchController.stop()
                    (stopwatchState == StopwatchState.Stopped) -> stopwatchController.resume()
                }
            }
        }
    }
}

@Composable
private fun TimerButton(
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


























