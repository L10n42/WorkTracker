package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.data.service.StopwatchService
import com.kappdev.worktracker.tracker_feature.data.service.StopwatchState
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
    val stopwatchState by stopwatchService.currentState
    val stopwatchActivityId by stopwatchService.activityId

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
        ) { scaffoldPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(scaffoldPadding),
                contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                items(activities, key = { it.id }) { activity ->
                    val isCurrentActivity = (stopwatchActivityId == activity.id)
                    ActivityCard(
                        activity = activity,
                        isActive = isCurrentActivity && stopwatchState == StopwatchState.Started
                    ) {
                        when {
                            (stopwatchState == StopwatchState.Idle) -> {
                                viewModel.stopwatchController.start(activity.id, activity.name)
                            }
                            (isCurrentActivity && stopwatchState == StopwatchState.Started) -> {
                                viewModel.stopwatchController.stop()
                            }
                            (isCurrentActivity && stopwatchState == StopwatchState.Stopped) -> {
                                viewModel.stopwatchController.resume()
                            }
                        }
                    }
                }
            }
        }
    }
}