package com.kappdev.worktracker.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.components.ActivityReviewScreen
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components.AddEditActivityScreen
import com.kappdev.worktracker.tracker_feature.presentation.countdown_timer.componets.CountdownTimerScreen
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.components.MainScreen
import com.kappdev.worktracker.tracker_feature.presentation.settings.components.SettingsScreen
import com.kappdev.worktracker.tracker_feature.presentation.splash_screen.components.SplashScreen
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
    countdownController: CountdownController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(
            Screen.Main.route,
            exitTransition = {
                when {
                    navigatesToward(Screen.AddEditActivity) -> slideOutLeft()
                    navigatesToward(Screen.ActivityReview) -> slideOutLeft()
                    navigatesToward(Screen.Settings) -> cornerScaleOut(Corner.BOTTOM_LEFT)
                    else -> null
                }
            },
            popEnterTransition = {
                when {
                    navigatesFrom(Screen.Settings) -> cornerScaleIn(Corner.TOP_LEFT)
                    navigatesFrom(Screen.ActivityReview) -> slideInRight()
                    navigatesFrom(Screen.AddEditActivity) -> slideInRight()
                    else -> null
                }
            }
        ) {
            MainScreen(navController, stopwatchService, countdownService)
        }

        composable(
            Screen.WorkStatistic.route,
            enterTransition = { screenFadeIn() },
            exitTransition = { screenFadeOut() },
            popEnterTransition = { screenFadeIn() },
            popExitTransition = { screenFadeOut() },
        ) {
            WorkStatisticScreen(navController)
        }

        composable(
            Screen.Settings.route,
            enterTransition = { cornerScaleIn(Corner.TOP_RIGHT) },
            exitTransition = { cornerScaleOut(Corner.BOTTOM_RIGHT) },
            popExitTransition = { cornerScaleOut(Corner.BOTTOM_RIGHT) },
            popEnterTransition = { cornerScaleIn(Corner.TOP_RIGHT) }
        ) {
            SettingsScreen(navController)
        }

        composable(
            Screen.SplashScreen.route,
            enterTransition = { screenFadeIn() },
            exitTransition = { screenFadeOut() }
        ) {
            SplashScreen {
                navController.navigate(startDestination)
            }
        }

        composable(
            Screen.StopwatchTimer.route,
            enterTransition = { slideInUp() },
            exitTransition = { slideOutDown() },
            popEnterTransition = { slideInUp() },
            popExitTransition = { slideOutDown() }
        ) {
            StopwatchTimerScreen(navController, stopwatchService, stopwatchController)
        }

        composable(
            Screen.CountdownTimer.route,
            enterTransition = { slideInUp() },
            exitTransition = { slideOutDown() },
            popEnterTransition = { slideInUp() },
            popExitTransition = { slideOutDown() }
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
            enterTransition = { slideInLeft() },
            popEnterTransition = { slideInRight() },
            exitTransition = {
                when {
                    navigatesToward(Screen.AddEditActivity) -> slideOutLeft()
                    else -> slideOutRight()
                }
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
            ),
            enterTransition = { slideInLeft() },
            exitTransition = { slideOutRight() },
            popExitTransition = { slideOutRight() }
        ) {
            val activityId = it.arguments?.getLong("activityId")
            AddEditActivityScreen(navController, activityId)
        }
    }
}

private fun screenFadeIn() = fadeIn(animationSpec = tween(ANIM_DURATION))
private fun screenFadeOut() = fadeOut(animationSpec = tween(ANIM_DURATION))

private fun slideInLeft() = slideInHorizontally(animationSpec = tween(ANIM_DURATION)) { it }
private fun slideInRight() = slideInHorizontally(animationSpec = tween(ANIM_DURATION)) { -it }

private fun slideOutLeft() = slideOutHorizontally(animationSpec = tween(ANIM_DURATION)) { -it }
private fun slideOutRight() = slideOutHorizontally(animationSpec = tween(ANIM_DURATION)) { it }


private enum class Corner { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

private fun Corner.getOrigin() = when (this) {
    Corner.TOP_LEFT -> TransformOrigin(0f, 0f)
    Corner.TOP_RIGHT -> TransformOrigin(1f, 0f)
    Corner.BOTTOM_LEFT -> TransformOrigin(0f, 1f)
    Corner.BOTTOM_RIGHT -> TransformOrigin(1f, 1f)
}

@OptIn(ExperimentalAnimationApi::class)
private fun cornerScaleIn(corner: Corner) = scaleIn(
    animationSpec = tween(ANIM_DURATION),
    transformOrigin = corner.getOrigin()
)

@OptIn(ExperimentalAnimationApi::class)
private fun cornerScaleOut(corner: Corner) = scaleOut(
    animationSpec = tween(ANIM_DURATION),
    transformOrigin = corner.getOrigin()
)


@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.slideInUp(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentScope.SlideDirection.Up,
        animationSpec = tween(ANIM_DURATION)
    )
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.slideOutDown(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentScope.SlideDirection.Down,
        animationSpec = tween(ANIM_DURATION)
    )
}



@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.navigatesToward(screen: Screen): Boolean {
    return getTargetRoute()?.contains(screen.route) ?: false
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.getTargetRoute(): String? {
    return this.targetState.destination.route
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.navigatesFrom(screen: Screen): Boolean {
    return getInitialRoute()?.contains(screen.route) ?: false
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.getInitialRoute(): String? {
    return this.initialState.destination.route
}

private const val ANIM_DURATION = 700