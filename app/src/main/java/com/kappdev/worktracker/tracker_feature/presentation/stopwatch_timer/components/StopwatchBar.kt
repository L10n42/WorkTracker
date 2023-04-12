package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.data.service.StopwatchService
import com.kappdev.worktracker.tracker_feature.data.service.StopwatchState
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.spacing
import com.kappdev.worktracker.ui.theme.StopRed

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxScope.StopwatchBar(
    navController: NavHostController,
    stopwatchService: StopwatchService,
    stopwatchController: StopwatchController
) {
    val textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onSurface)
    val time by stopwatchService.time
    val activityName by stopwatchService.activityName
    val stopwatchState by stopwatchService.currentState
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = stopwatchService.currentState.value != StopwatchState.Idle && currentRoute != Screen.StopwatchTimer.route,
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Surface(
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.customShape.medium,
            elevation = MaterialTheme.elevation.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MaterialTheme.spacing.small)
                .height(56.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                navController.navigate(Screen.StopwatchTimer.route)
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(MaterialTheme.spacing.medium))
                Text(
                    text = activityName,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(MaterialTheme.spacing.small))
                AnimatedTimer(
                    time = time,
                    style = textStyle,
                    modifier = Modifier.wrapContentWidth()
                )
                
                IconButton(
                    onClick = stopwatchController::finish
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Stop,
                        contentDescription = "arrow to stop tracking the activity time",
                        tint = StopRed
                    )
                }

                IconButton(
                    onClick = {
                        when {
                            (stopwatchState == StopwatchState.Started) -> stopwatchController.stop()
                            (stopwatchState == StopwatchState.Stopped) -> stopwatchController.resume()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (stopwatchState == StopwatchState.Started) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = "arrow to start tracking activity time",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}



























