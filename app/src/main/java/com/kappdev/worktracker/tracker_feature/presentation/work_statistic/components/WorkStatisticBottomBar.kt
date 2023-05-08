package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kappdev.worktracker.tracker_feature.presentation.common.components.DaySwitcher
import com.kappdev.worktracker.ui.elevation
import java.time.LocalDate

@Composable
fun WorkStatisticBottomBar(
    date: LocalDate,
    updateData: (date: LocalDate) -> Unit
) {
    BottomAppBar(
        contentPadding = PaddingValues(0.dp),
        backgroundColor = Color.Transparent,
        elevation = MaterialTheme.elevation.medium
    ) {
        DaySwitcher(
            date = date,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.surface),
            changeDate = updateData
        )
    }
}