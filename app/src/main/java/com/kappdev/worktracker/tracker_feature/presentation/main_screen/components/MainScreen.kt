package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    stopwatchService: StopwatchService,
    countdownService: CountdownService,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val stopwatchState by stopwatchService.currentState
    val countdownState by countdownService.currentState
    val stopwatchActivityId by stopwatchService.activityId
    val countdownActivityId by countdownService.activityId

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { state ->
            if (state == ModalBottomSheetValue.Hidden) viewModel.clearSheet()
            true
        }
    )
    val scaffoldState = rememberScaffoldState()

    val navigate = viewModel.navigate.value
    val activities = viewModel.activities.value
    val bottomSheet = viewModel.bottomSheet.value

    LaunchedEffect(key1 = bottomSheet) {
        if (bottomSheet != null) sheetState.show()
        if (bottomSheet == null && sheetState.isVisible) sheetState.hide()
    }

    LaunchedEffect(key1 = navigate) {
        if (navigate != null) {
            navController.navigate(navigate)
            viewModel.clearNavigationRoute()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            if (bottomSheet != null) {
                MainScreenBottomSheetController(bottomSheet, viewModel, countdownState)
            } else {
                VerticalSpace(1.dp)
            }
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MainScreenTopBar(viewModel)
            }
        ) { scaffoldPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding),
                contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                items(activities, key = { it.id }) { activity ->
                    val isCurrentActivity = (stopwatchActivityId == activity.id)
                    ActivityCard(
                        activity = activity,
                        isActive = isCurrentActivity && stopwatchState == ServiceState.Started,
                        onStart = {
                            when {
                                (stopwatchState == ServiceState.Idle && countdownState == ServiceState.Idle) -> {
                                    viewModel.stopwatchController.start(activity.id, activity.name)
                                }
                                (isCurrentActivity && stopwatchState == ServiceState.Started) -> {
                                    viewModel.stopwatchController.stop()
                                }
                                (isCurrentActivity && stopwatchState == ServiceState.Stopped) -> {
                                    viewModel.stopwatchController.resume()
                                }
                            }
                        },
                        onStartTimer = {
                            viewModel.openSheet(
                                MainScreenBottomSheet.TimePicker(activity.id, activity.name)
                            )
                        }
                    )
                }
            }
        }
    }
}