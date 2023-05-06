package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import com.kappdev.worktracker.tracker_feature.presentation.work_statistic.WorkStatisticViewModel
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WorkStatisticScreen(
    navController: NavHostController,
    viewModel: WorkStatisticViewModel = hiltViewModel()
) {
    val data = viewModel.data.value
    val dataState = viewModel.dataState.value

    LaunchedEffect(key1 = true) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {

        }
    ) { scaffoldPadding ->

        AnimatedContent(
            targetState = dataState,
            transitionSpec = {
                fadeIn() with fadeOut()
            }
        ) { state ->
            when(state) {
                DataState.READY -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding),
                        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        item {
                            PieChart(
                                data = data,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = MaterialTheme.spacing.large),
                                animDuration = ANIM_DURATION
                            )
                        }

                        items(data) { value ->
                            PieCard(
                                data = value,
                                modifier = Modifier.fillMaxWidth(),
                                animDuration = ANIM_DURATION
                            )
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    }
}

private const val ANIM_DURATION = 1000
