package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.ActivityReviewViewModel
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.GraphDataState
import com.kappdev.worktracker.tracker_feature.presentation.common.components.CalendarView
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ActivityReviewScreen(
    navController: NavHostController,
    activityId: Long,
    viewModel: ActivityReviewViewModel = hiltViewModel()
) {
    val navigate = viewModel.navigate.value
    val graphDate = viewModel.graphDate.value
    val activity = viewModel.currentActivity.value
    val dailyGraphData = viewModel.dailyGraphData.value
    val graphDataState = viewModel.graphDataState.value
    val totalDailyWorkingTime = viewModel.totalDailyWorkingTime.value
    val calendarData = viewModel.calendarData.value
    val calendarDate = viewModel.calendarDate.value

    val graphModifier = Modifier
        .fillMaxWidth()
        .padding(MaterialTheme.spacing.medium)
        .height(300.dp)

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
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.wrapContentSize()
            ) {
                AnimatedContent(
                    targetState = graphDataState,
                    transitionSpec = {
                        fadeIn() with fadeOut()
                    }
                ) { state ->
                    when (state) {
                        GraphDataState.LOADING -> {
                            LoadingGraph(modifier = graphModifier)
                        }
                        else -> {
                            CustomDailyGraph(
                                value = dailyGraphData,
                                totalTime = totalDailyWorkingTime,
                                modifier = graphModifier
                            )
                        }
                    }
                }
            }

            DaySwitcher(
                date = graphDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                changeDate = viewModel::setDateAndUpdate
            )

            CalendarView(
                date = calendarDate,
                data = calendarData,
                target = activity?.targetInSec ?: 0,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                changeDate = viewModel::updateCalendarWith
            )
        }
    }
}