package com.kappdev.worktracker.tracker_feature.presentation.stopwatch_timer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.TimerButtons
import com.kappdev.worktracker.ui.spacing

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
                    .padding(horizontal = MaterialTheme.spacing.large),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }

        TimerButtons(
            state = stopwatchState,
            onStop = stopwatchController::stop,
            onResume = stopwatchController::resume,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(all = MaterialTheme.spacing.large),
            onFinish = {
                stopwatchController.finish()
                navController.navigate(Screen.Main.route)
            }
        )
    }
}




























