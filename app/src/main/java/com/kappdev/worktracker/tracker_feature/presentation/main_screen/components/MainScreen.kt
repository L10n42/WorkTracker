package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.animation.*
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
import com.kappdev.worktracker.tracker_feature.presentation.common.components.EmptyScreen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.LoadingScreen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.NoDataScreen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenState
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
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

    val navigate = viewModel.navigate.value
    val activities = viewModel.activities.value
    val bottomSheet = viewModel.bottomSheet.value
    val screenState = viewModel.screenState.value
    val dataState = viewModel.dataState.value
    val dialog = viewModel.dialog.value
    val selectedActivities = viewModel.selectedActivities

    LaunchedEffect(key1 = true, block = { viewModel.launch() })

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

    if (!sheetState.isVisible && bottomSheet is MainScreenBottomSheet.Sort) viewModel.clearSheet()
    MainScreenDialogController(dialog, viewModel)
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
            topBar = {
                TopBarController(screenState, viewModel)
            }
        ) { scaffoldPadding ->

            AnimatedContent(
                targetState = dataState,
                transitionSpec = {
                    fadeIn() with fadeOut()
                }
            ) { animDataState ->
                when (animDataState) {
                    DataState.NO_DATA -> NoDataScreen()
                    DataState.LOADING -> LoadingScreen()
                    DataState.IDLE -> EmptyScreen()
                    DataState.READY -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(scaffoldPadding),
                            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraSmall),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                        ) {
                            items(activities, key = { it.id }) { activity ->
                                val isCurrentActivity = (stopwatchActivityId == activity.id) || (countdownActivityId == activity.id)
                                ActivityCard(
                                    activity = activity,
                                    viewModel = viewModel,
                                    isSelected = activity in selectedActivities,
                                    isSelectionMode = screenState == MainScreenState.SELECTION_MODE,
                                    isStopwatchActive = isCurrentActivity && stopwatchState == ServiceState.Started,
                                    isCountdownActive = isCurrentActivity && countdownState != ServiceState.Idle,
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
                                        if (stopwatchState == ServiceState.Idle && countdownState == ServiceState.Idle) {
                                            viewModel.openSheet(
                                                MainScreenBottomSheet.TimePicker(activity.id, activity.name)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}