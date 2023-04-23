package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@Composable
fun CustomDailyGraph(
    value: Map<Int, Long>,
    totalTime: String,
    modifier: Modifier = Modifier,
    maxValue: Long = value.maxByOrNull { it.value }?.value ?: 0L,
) {
    Column(
        modifier = modifier
            .border(
                color = MaterialTheme.colors.onSurface,
                width = 1.dp,
                shape = MaterialTheme.customShape.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .fillMaxHeight(0.8f)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(value.toList()) { item ->
                    GraphColumn(item, maxValue)
                }
            }

            Spacer(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 19.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onBackground)
            )
        }

        TotalSection(
            totalTime = totalTime,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun TotalSection(
    totalTime: String,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.total_work_time)
    val totalTimeString = buildAnnotatedString {
        append("$title ")
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold)
        ) {
            append(totalTime.trimStart())
        }
    }

    Text(
        text = totalTimeString,
        fontSize = 16.sp,
        modifier = modifier,
        color = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun GraphColumn(
    value: Pair<Int, Long>,
    maxValue: Long,
    modifier: Modifier = Modifier
) {
    var timeValue by remember { mutableStateOf("") }

    var targetFraction by remember { mutableStateOf(0f) }
    var columnSize by remember { mutableStateOf(Size.Zero) }
    val columnHeight = with(LocalDensity.current) { columnSize.height.toDp() }
    val valuePadding = columnHeight + 24.dp

    val animFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    LaunchedEffect(key1 = value) {
        val time = value.second / 60f
        timeValue = when {
            time > 0 && time < 1 -> "${value.second}s"
            time > 1 -> "${time.toInt()}m"
            else -> ""
        }
    }

    LaunchedEffect(key1 = value, key2 = maxValue) {
        targetFraction = if (maxValue > 0) {
            (value.second / 60f) / (maxValue / 60f)
        } else {
            0f
        }
    }

    Box(
        modifier = modifier
            .width(16.dp)
            .fillMaxHeight()
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp, top = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(animFraction)
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                )
                .onGloballyPositioned { coordinates ->
                    columnSize = coordinates.size.toSize()
                }
        )

        Text(
            text = timeValue,
            fontSize = 8.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = valuePadding)
        )

        Text(
            text = value.first.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}