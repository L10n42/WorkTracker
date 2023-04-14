package com.kappdev.worktracker.tracker_feature.presentation.countdonw_timer.componets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.TimerButtons
import com.kappdev.worktracker.ui.spacing

@Composable
fun CountdownTimerScreen(
    navController: NavHostController,
    countdownService: CountdownService,
    countdownController: CountdownController
) {
    val activityName by countdownService.activityName
    val countdownState by countdownService.currentState

    LaunchedEffect(key1 = countdownState) {
        if (countdownState == ServiceState.Idle) navController.navigate(Screen.Main.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        val configuration = LocalConfiguration.current
        val timerPadding = when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> MaterialTheme.spacing.medium
            else ->  MaterialTheme.spacing.extraLarge * 2
        }
        LargeCountdownTimer(
            countdownService = countdownService,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = timerPadding)
                .size(270.dp)
        )

        TimerButtons(
            state = countdownState,
            onStop = countdownController::stop,
            onResume = countdownController::resume,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(all = MaterialTheme.spacing.large),
            onFinish = {
                countdownController.finish()
                navController.navigate(Screen.Main.route)
            }
        )
    }
}