package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.animation.*
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
import com.kappdev.worktracker.tracker_feature.presentation.common.components.LoadingScreen
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

    LaunchedEffect(key1 = true) {
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
            }
        ) { state ->
            when(state) {
                DataState.READY -> Content(data, totalTime) { route ->
                    navController.navigate(route)
                }
                DataState.NO_DATA -> EmptyWorkScreen(date)
                else -> LoadingScreen()
            }
        }
    }
}

@Composable
private fun Content(
    data: List<ReportData>,
    totalTime: Long,
    navigate: (route: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        item {
            BackButton(Icons.Default.Close) {
                navigate(Screen.Main.route)
            }
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
                animDuration = ANIM_DURATION
            )
        }
    }
}

@Composable
fun EmptyWorkScreen(date: LocalDate) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

private const val ANIM_DURATION = 1000
