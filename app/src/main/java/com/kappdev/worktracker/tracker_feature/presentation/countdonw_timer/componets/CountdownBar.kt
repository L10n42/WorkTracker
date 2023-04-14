package com.kappdev.worktracker.tracker_feature.presentation.countdonw_timer.componets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.tracker_feature.presentation.common.components.HorizontalSpace
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer_bar.FinishButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer_bar.StopResumeButton
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = countdownService.currentState.value != ServiceState.Idle && currentRoute != Screen.StopwatchTimer.route,
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
                //navController.navigate(Screen.StopwatchTimer.route)
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalSpace(MaterialTheme.spacing.medium)
                Text(
                    text = activityName,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
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
        }
    }
}