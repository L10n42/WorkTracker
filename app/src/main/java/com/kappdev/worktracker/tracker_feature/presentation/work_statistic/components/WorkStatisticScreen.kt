package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.ReportData
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.SimpleLoadingScreen
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.DataState
import com.kappdev.worktracker.tracker_feature.presentation.work_statistic.WorkStatisticViewModel
import com.kappdev.worktracker.ui.spacing
import java.time.LocalDate

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WorkStatisticScreen(
    navController: NavHostController,
    viewModel: WorkStatisticViewModel = hiltViewModel()
) {
    val data = viewModel.data.value
    val date = viewModel.date.value
    val totalTime = viewModel.totalTime.value
    val dataState = viewModel.dataState.value

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Scaffold(
        bottomBar = {
            WorkStatisticBottomBar(date) { newDate ->
                viewModel.setDate(newDate)
                viewModel.getData()
            }
        }
    ) { scaffoldPadding ->
        AnimatedContent(
            targetState = dataState,
            modifier = Modifier.padding(scaffoldPadding),
            transitionSpec = {
                fadeIn() with fadeOut()
            },
            label = "screen state transition"
        ) { state ->
            when(state) {
                DataState.READY -> Content(data, totalTime) {
                    navController.popBackStack()
                }
                DataState.NO_DATA -> EmptyWorkScreen(date) {
                    navController.popBackStack()
                }
                else -> SimpleLoadingScreen()
            }
        }
    }
}

@Composable
private fun Content(
    data: List<ReportData>,
    totalTime: Long,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        item {
            BackButton(
                icon = Icons.Default.Close,
                onClick = onBack
            )
        }

        item {
            PieChart(
                data = data,
                totalTime = totalTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.large),
                animDuration = ANIM_DURATION
            )
        }

        items(data) { value ->
            PieCard(
                data = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                animationSpec = tween(
                    durationMillis = ANIM_DURATION,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    }
}

@Composable
fun EmptyWorkScreen(
    date: LocalDate,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackButton(
            icon = Icons.Default.Close,
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onBack
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.DataUsage,
                contentDescription = "pie chart icon",
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier.size(128.dp)
            )
            Text(
                text = "You didn't work on $date.",
                fontSize = 18.sp,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

private const val ANIM_DURATION = 1000
