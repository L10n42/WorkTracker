package com.kappdev.worktracker.tracker_feature.presentation.work_statistic.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.domain.model.PieChartData
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil
import com.kappdev.worktracker.tracker_feature.presentation.common.components.HorizontalSpace
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@Composable
fun PieCard(
    modifier: Modifier = Modifier,
    data: PieChartData,
    animDuration: Int
) {
    var targetPercent by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(key1 = data) {
        targetPercent = data.percent
    }

    val animPercent by animateFloatAsState(
        targetValue = targetPercent * 100,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    val animProgress by animateFloatAsState(
        targetValue = targetPercent,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PieColor(data.color)

        HorizontalSpace(MaterialTheme.spacing.medium)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PieText(
                    text = data.activity.name,
                    modifier = Modifier.weight(1f)
                )
                HorizontalSpace(MaterialTheme.spacing.small)
                PieText(
                    text = TimeUtil.splitTime(seconds = data.timeValue, shortForm = true)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PieProgress(
                    progress = animProgress,
                    color = data.color,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                PiePercent(percent = animPercent)
            }
        }
    }
}

@Composable
private fun PiePercent(
    percent: Float,
    modifier: Modifier = Modifier
) {
    val formattedPercent = String.format("%.1f", percent)
    Text(
        text = "$formattedPercent%",
        fontSize = 14.sp,
        color = MaterialTheme.colors.onBackground,
        modifier = modifier
    )
}

@Composable
private fun PieProgress(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        color = color,
        progress = progress,
        modifier = modifier
            .height(4.dp)
            .clip(CircleShape),
        backgroundColor = Color.Transparent
    )
}

@Composable
private fun PieText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun PieColor(color: Color) {
    Spacer(
        modifier = Modifier
            .size(32.dp)
            .background(
                color = color,
                shape = MaterialTheme.customShape.medium
            )
    )
}




