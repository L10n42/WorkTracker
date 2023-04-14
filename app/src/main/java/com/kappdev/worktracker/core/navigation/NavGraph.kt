package com.kappdev.worktracker.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components.AddEditActivityScreen
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.components.MainScreen
import com.kappdev.worktracker.tracker_feature.presentation.stopwatch_timer.components.StopwatchTimerScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    stopwatchService: StopwatchService,
    countdownService: CountdownService,
    stopwatchController: StopwatchController,
    countdownController: CountdownController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(
            Screen.Main.route,
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 300))
            }
        ) {
            MainScreen(navController, stopwatchService, countdownService)
        }

        composable(
            Screen.StopwatchTimer.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 300))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeOut(animationSpec = tween(durationMillis = 300))
            }
        ) {
            StopwatchTimerScreen(navController, stopwatchService, stopwatchController)
        }

        composable(
            route = Screen.AddEditActivity.route + "?activityId={activityId}",
            arguments = listOf(
                navArgument("activityId") {
                    type = NavType.LongType
                    defaultValue = 0
                }
            )
        ) {
            val activityId = it.arguments?.getLong("activityId")
            AddEditActivityScreen(navController, activityId)
        }
    }
}