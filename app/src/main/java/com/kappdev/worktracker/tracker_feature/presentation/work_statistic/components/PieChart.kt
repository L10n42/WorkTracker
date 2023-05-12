package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.domain.model.ReportData
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: List<ReportData>,
    totalTime: Long,
    pieSize: Dp = 200.dp,
    chartBarWidth: Dp = 32.dp,
    animDuration: Int,
) {
    var animationPlayed by remember { mutableStateOf(false) }
    var totalTimeString by remember { mutableStateOf("") }
    var lastValue = remember { 0f }

    LaunchedEffect(key1 = totalTime) {
        totalTimeString = TimeUtil.splitTime(totalTime, shortForm = true)
    }

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    val animSize by animateDpAsState(
        targetValue = if (animationPlayed) pieSize else 0.dp,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )
    
    val animRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(animSize),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .size(pieSize)
                    .rotate(animRotation)
            ) {
                data.forEach { pieChart ->
                    val value = 360 * pieChart.percent
                    drawArc(
                        color = pieChart.color,
                        startAngle = lastValue,
                        sweepAngle = value + 1,
                        useCenter = false,
                        style = Stroke(width = chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }

            Text(
                text = totalTimeString,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}
