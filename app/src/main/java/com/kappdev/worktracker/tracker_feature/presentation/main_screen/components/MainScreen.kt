package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.data.service.stopwatch.StopwatchService
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.presentation.common.components.CustomLoadingScreen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.NoDataScreen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenState
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    stopwatchService: StopwatchService,
    countdownService: CountdownService,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val stopwatchState by stopwatchService.currentState
    val countdownState by countdownService.currentState
    val stopwatchActivityId by stopwatchService.activityId
    val countdownActivityId by countdownService.activityId

    val activities = viewModel.activities.value
    val screenState = viewModel.screenState.value
    val dataState = viewModel.dataState.value
    val dialog = viewModel.dialog.value
    val selectedActivities = viewModel.selectedActivities

    var currentSheet by remember { mutableStateOf<MainScreenBottomSheet?>(null) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    fun closeSheet() = scope.launch { sheetState.hide() }

    fun openSheet(sheet: MainScreenBottomSheet) = scope.launch {
        currentSheet = sheet
        sheetState.show()
    }

    if (viewModel.showServiceInfo.value) {
        openSheet(MainScreenBottomSheet.ServiceInfo)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    LaunchedEffect(Unit) {
        viewModel.launch()
        if (needNotificationPermission(context)) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (!sheetState.isVisible) currentSheet = null
    MainScreenDialogController(dialog, viewModel)
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            if (currentSheet == null) VerticalSpace(1.dp)
            currentSheet?.let { sheet ->
                MainScreenBottomSheetController(sheet, viewModel, countdownState, ::closeSheet)
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBarController(
                    screenState = screenState,
                    viewModel = viewModel,
                    onNavigate = navController::navigate,
                    openSheet = ::openSheet
                )
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
                    DataState.LOADING, DataState.IDLE -> CustomLoadingScreen()
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
                                    onNavigate = navController::navigate,
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
                                            openSheet(
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

private fun needNotificationPermission(context: Context): Boolean {
    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
}