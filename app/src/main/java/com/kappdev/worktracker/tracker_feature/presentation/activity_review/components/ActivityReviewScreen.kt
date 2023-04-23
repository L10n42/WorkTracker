package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.ActivityReviewViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.CustomDailyGraph
import com.kappdev.worktracker.ui.spacing

@Composable
fun ActivityReviewScreen(
    navController: NavHostController,
    activityId: Long,
    viewModel: ActivityReviewViewModel = hiltViewModel()
) {
    val navigate = viewModel.navigate.value

    LaunchedEffect(key1 = navigate) {
        if (navigate != null) {
            navController.navigate(navigate)
            viewModel.clearNavigationRoute()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.getDataFor(activityId)
    }

    Scaffold(
        topBar = {
            ActivityReviewTopBar(viewModel)
        }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            CustomDailyGraph(
                value = viewModel.dailyGraphData,
                totalTime = viewModel.totalDailyWorkingTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
                    .height(270.dp)
            )
        }
    }
}