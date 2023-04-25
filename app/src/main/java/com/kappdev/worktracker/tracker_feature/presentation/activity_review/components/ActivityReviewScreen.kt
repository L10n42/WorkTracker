package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.ActivityReviewViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.CustomDailyGraph
import com.kappdev.worktracker.tracker_feature.presentation.common.components.DaySwitcher
import com.kappdev.worktracker.ui.spacing
import java.time.LocalDate

@Composable
fun ActivityReviewScreen(
    navController: NavHostController,
    activityId: Long,
    viewModel: ActivityReviewViewModel = hiltViewModel()
) {
    val navigate = viewModel.navigate.value
    var date by remember {
        mutableStateOf(LocalDate.now())
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            CustomDailyGraph(
                value = viewModel.dailyGraphData,
                totalTime = viewModel.totalDailyWorkingTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .height(270.dp)
            )

            DaySwitcher(
                date = date,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                changeDate = { newDate ->
                    date = newDate
                }
            )
        }
    }
}