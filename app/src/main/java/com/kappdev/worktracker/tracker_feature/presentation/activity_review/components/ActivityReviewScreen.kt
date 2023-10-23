package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.ActivityReviewViewModel
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.GraphDataState
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.GraphViewState
import com.kappdev.worktracker.tracker_feature.presentation.common.components.CalendarView
import com.kappdev.worktracker.tracker_feature.presentation.common.components.DaySwitcher
import com.kappdev.worktracker.tracker_feature.presentation.common.components.PeriodSwitcher
import com.kappdev.worktracker.ui.customShape
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
    val dailyGraphData = viewModel.graphData.value
    val graphDataState = viewModel.graphDataState.value
    val totalDailyWorkingTime = viewModel.totalTime.value
    val calendarData = viewModel.calendarData.value
    val calendarDate = viewModel.calendarDate.value
    val graphViewState = viewModel.graphViewState.value

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
            AnimatedContent(
                targetState = graphDataState,
                transitionSpec = {
                    fadeIn() with fadeOut()
                }, label = ""
            ) { state ->
                when (state) {
                    GraphDataState.LOADING -> {
                        LoadingGraph(modifier = graphModifier)
                    }
                    else -> {
                        CustomDailyGraph(
                            value = dailyGraphData,
                            totalTime = totalDailyWorkingTime,
                            modifier = graphModifier,
                            viewModel = viewModel
                        )
                    }
                }
            }

            val switcherModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onSurface,
                    shape = MaterialTheme.customShape.medium
                )
            AnimatedContent(
                targetState = graphViewState,
                transitionSpec = {
                    fadeIn() with fadeOut()
                }, label = ""
            ) { view ->
                when (view) {
                    GraphViewState.DAY -> {
                        DaySwitcher(
                            date = graphDate,
                            modifier = switcherModifier,
                            changeDate = viewModel::updateGraphWith
                        )
                    }
                    else -> {
                        PeriodSwitcher(
                            date = graphDate,
                            viewState = graphViewState,
                            modifier = switcherModifier,
                            changeDate = viewModel::updateGraphWith
                        )
                    }
                }
            }

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