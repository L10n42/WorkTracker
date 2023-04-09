package com.kappdev.worktracker.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kappdev.worktracker.tracker_feature.domain.service.StopwatchService
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components.AddEditActivityScreen
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.components.MainScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    stopwatchService: StopwatchService
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController, stopwatchService)
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