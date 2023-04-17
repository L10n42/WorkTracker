package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenState
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBarController(
    screenState: MainScreenState,
    viewModel: MainScreenViewModel
) {
    AnimatedContent(
        targetState = screenState,
        transitionSpec = {
            slideInVertically() with slideOutVertically()
        }
    ) { animScreenState ->
        when (animScreenState) {
            MainScreenState.SELECTION_MODE -> {
                SelectionTopBar(viewModel)
            }
            MainScreenState.NORMAL_MODE -> {
                MainScreenTopBar(viewModel)
            }
        }
    }
}