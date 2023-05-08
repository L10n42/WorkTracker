package com.kappdev.worktracker.tracker_feature.presentation.countdown_timer.componets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.tracker_feature.presentation.common.components.HorizontalSpace
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.FinishButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.StopResumeButton
import com.kappdev.worktracker.tracker_feature.presentation.common.util.TimerAnimationDirection
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxScope.CountdownBar(
    navController: NavHostController,
    countdownService: CountdownService,
    countdownController: CountdownController
) {
    val textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onSurface)
    val time by countdownService.time
    val activityName by countdownService.activityName
    val countdownState by countdownService.currentState
    val completionPercentage by countdownService.completionPercentage
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val serviceIsStarted = countdownService.currentState.value != ServiceState.Idle
    val isNotStatisticScreen = currentRoute != Screen.WorkStatistic.route
    val isNotCountdownTimerScreen = currentRoute != Screen.CountdownTimer.route

    AnimatedVisibility(
        visible = serviceIsStarted && isNotCountdownTimerScreen && isNotStatisticScreen,
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
                navController.navigate(Screen.CountdownTimer.route)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalSpace(MaterialTheme.spacing.medium)
                    Text(
                        text = activityName,
                        style = textStyle,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis
                    )
                    HorizontalSpace(MaterialTheme.spacing.small)

                    AnimatedTimer(
                        time = time,
                        style = textStyle,
                        modifier = Modifier.wrapContentWidth(),
                        direction = TimerAnimationDirection.Bottom
                    )

                    FinishButton(onClick = countdownController::finish)

                    StopResumeButton(
                        state = countdownState,
                        onStop = countdownController::stop,
                        onResume = countdownController::resume
                    )
                }

                LinearProgressIndicator(
                    progress = completionPercentage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .padding(horizontal = MaterialTheme.spacing.small)
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape),
                    color = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.primary.copy(0.24f)
                )
            }
        }
    }
}