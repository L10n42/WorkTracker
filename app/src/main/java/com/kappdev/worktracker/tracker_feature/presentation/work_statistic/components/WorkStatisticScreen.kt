package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.PieChartData
import com.kappdev.worktracker.tracker_feature.domain.util.RandomColorGenerator.getBrightColor
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.work_statistic.WorkStatisticViewModel
import com.kappdev.worktracker.ui.spacing

@Composable
fun WorkStatisticScreen(
    navController: NavHostController,
    viewModel: WorkStatisticViewModel = hiltViewModel()
) {
    val testData = listOf(
        PieChartData(
            Activity(id = 1, name = "test - 1", creationTimestamp = 0, targetInSec = 0),
            color = getBrightColor(),
            percent = 0.34f,
            timeValue = 6000
        ),
        PieChartData(
            Activity(id = 2, name = "test - 2", creationTimestamp = 0, targetInSec = 0),
            color = getBrightColor(),
            percent = 0.21f,
            timeValue = 32000
        ),
        PieChartData(
            Activity(id = 3, name = "test - 3", creationTimestamp = 0, targetInSec = 0),
            color = getBrightColor(),
            percent = 0.64f,
            timeValue = 3000
        ),
    )

    Scaffold(
        topBar = {

        }
    ) { scaffoldPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(scaffoldPadding),
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item {
                VerticalSpace(MaterialTheme.spacing.extraLarge)
                PieChart(
                    modifier = Modifier.fillMaxWidth(),
                    data = testData,
                    animDuration = ANIM_DURATION
                )
                VerticalSpace(MaterialTheme.spacing.large)
            }

            items(testData) { value ->
                PieCard(
                    data = value,
                    modifier = Modifier.fillMaxWidth(),
                    animDuration = ANIM_DURATION
                )
            }
        }
    }
}

private const val ANIM_DURATION = 1000
