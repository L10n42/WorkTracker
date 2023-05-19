package com.kappdev.worktracker.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.kappdev.worktracker.core.data.repository.SettingsRepositoryImpl
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.components.ActivityReviewScreen
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components.AddEditActivityScreen
import com.kappdev.worktracker.tracker_feature.presentation.countdown_timer.componets.CountdownTimerScreen
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.components.MainScreen
import com.kappdev.worktracker.tracker_feature.presentation.privacy.components.PrivacyScreen
import com.kappdev.worktracker.tracker_feature.presentation.settings.components.SettingsScreen
import com.kappdev.worktracker.tracker_feature.presentation.stopwatch_timer.components.StopwatchTimerScreen
import com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components.WorkStatisticScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    stopwatchService: StopwatchService,
    countdownService: CountdownService,
    stopwatchController: StopwatchController,
    countdownController: CountdownController
) {
    val setting = SettingsRepositoryImpl(LocalContext.current)
    AnimatedNavHost(
        navController = navController,
        startDestination = if (setting.privacyEnable()) {
            Screen.Privacy.route
        } else {
            startDestination
        }
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController, stopwatchService, countdownService)
        }

        composable(Screen.WorkStatistic.route) {
            WorkStatisticScreen(navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }

        composable(Screen.Privacy.route) {
            PrivacyScreen {
                navController.navigate(startDestination)
            }
        }

        composable(
            Screen.StopwatchTimer.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                ) + fadeIn(
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    initialAlpha = 0.5f
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                ) + fadeOut(
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    targetAlpha = 0.5f
                )
            }
        ) {
            StopwatchTimerScreen(navController, stopwatchService, stopwatchController)
        }

        composable(
            Screen.CountdownTimer.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                ) + fadeIn(
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    initialAlpha = 0.5f
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                ) + fadeOut(
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    targetAlpha = 0.5f
                )
            }
        ) {
            CountdownTimerScreen(navController, countdownService, countdownController)
        }

        composable(
            route = Screen.ActivityReview.route + "?activityId={activityId}",
            arguments = listOf(
                navArgument("activityId") {
                    type = NavType.LongType
                    defaultValue = 0
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
            }
        ) {
            val activityId = it.arguments?.getLong("activityId")
            activityId?.let {
                ActivityReviewScreen(navController, activityId)
            } ?: navController.popBackStack()
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