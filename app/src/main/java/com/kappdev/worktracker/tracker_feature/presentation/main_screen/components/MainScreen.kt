package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.domain.service.StopwatchHelper
import com.kappdev.worktracker.tracker_feature.domain.service.StopwatchService
import com.kappdev.worktracker.tracker_feature.domain.service.StopwatchState
import com.kappdev.worktracker.tracker_feature.domain.util.StopwatchConstants
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    stopwatchService: StopwatchService,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val hours by stopwatchService.hours
    val minutes by stopwatchService.minutes
    val seconds by stopwatchService.seconds
    val stopwatchState by stopwatchService.currentState

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()

    val navigate = viewModel.navigate.value
    val activities = viewModel.activities.value

    LaunchedEffect(key1 = navigate) {
        if (navigate != null) {
            navController.navigate(navigate)
            viewModel.clearNavigationRoute()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = MaterialTheme.customShape.small,
        sheetContent = {
            // TODO (add the bottom sheet controller here for managing different sheets)
            Spacer(Modifier.size(1.dp))
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MainScreenTopBar(viewModel)
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                items(activities, key = { it.id }) { activity ->
                    val isCurrentActivity = stopwatchService.activityId.value == activity.id
                    ActivityCard(
                        activity = activity,
                        isActive = isCurrentActivity && stopwatchState == StopwatchState.Started
                    ) {
                        if (stopwatchState == StopwatchState.Idle) {
                            StopwatchHelper.startForegroundService(context, activity.id)
                        } else if (isCurrentActivity) {
                            StopwatchHelper.triggerForegroundService(
                                context = context,
                                action = if (stopwatchState == StopwatchState.Started) {
                                    StopwatchConstants.ACTION_SERVICE_STOP
                                } else {
                                    StopwatchConstants.ACTION_SERVICE_START
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}