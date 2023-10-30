package com.kappdev.worktracker.tracker_feature.presentation.countdown_timer.componets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.timer.TimerButtons
import com.kappdev.worktracker.ui.spacing

@Composable
fun CountdownTimerScreen(
    navController: NavHostController,
    countdownService: CountdownService,
    countdownController: CountdownController
) {
    val countdownState by countdownService.currentState

    LaunchedEffect(countdownState) {
        if (countdownState == ServiceState.Idle) navController.popBackStack()
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
                navController.popBackStack()
            }
        )

        BackButton(
            Icons.Rounded.ArrowForwardIos,
            modifier = Modifier
                .padding(8.dp)
                .rotate(90f)
        ) {
            navController.popBackStack()
        }
    }
}