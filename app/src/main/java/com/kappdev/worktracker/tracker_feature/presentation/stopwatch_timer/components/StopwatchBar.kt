package com.kappdev.worktracker.tracker_feature.presentation.stopwatch_timer.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.tracker_feature.presentation.common.components.HorizontalSpace
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.FinishButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.StopResumeButton
import com.kappdev.worktracker.tracker_feature.presentation.common.util.verticalDraggableInParent
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.spacing

private val NotAllowedRoutes = listOf(
    Screen.WorkStatistic.route,
    Screen.StopwatchTimer.route,
    Screen.SplashScreen.route
)

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

    val serviceIsStarted = stopwatchService.currentState.value != ServiceState.Idle

    AnimatedVisibility(
        visible = serviceIsStarted && (currentRoute !in NotAllowedRoutes),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .verticalDraggableInParent(),
        enter = fadeIn() + slideInVertically { it },
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Surface(
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.customShape.medium,
            elevation = MaterialTheme.elevation.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small)
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
                    modifier = Modifier.wrapContentWidth()
                )

                FinishButton(onClick = stopwatchController::finish)

                StopResumeButton(
                    state = stopwatchState,
                    onStop = stopwatchController::stop,
                    onResume = stopwatchController::resume
                )
            }
        }
    }
}



























