package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.kappdev.worktracker.tracker_feature.domain.util.TimeUtil
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.GraphViewState
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@Composable
fun CustomGraph(
    value: Map<String, Long>,
    totalTime: String,
    viewState: GraphViewState,
    modifier: Modifier = Modifier,
    maxValue: Long = value.maxByOrNull { it.value }?.value ?: 0L,
    onViewChange: (viewState: GraphViewState) -> Unit
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = MaterialTheme.customShape.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GraphViewSelector(
            modifier = Modifier.align(Alignment.End),
            selected = viewState,
            onViewChange = onViewChange
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .fillMaxHeight(0.85f)
        ) {
            val columnSpace by animateDpAsState(
                targetValue = if (viewState == GraphViewState.WEEK) 12.dp else 4.dp,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(columnSpace)
            ) {
                items(value.toList()) { item ->
                    GraphColumn(item, maxValue, viewState = viewState)
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
    value: Pair<String, Long>,
    maxValue: Long,
    modifier: Modifier = Modifier,
    viewState: GraphViewState
) {
    var timeValue by remember { mutableStateOf("") }
    var targetTextRotation by remember { mutableStateOf(0f) }

    var targetFraction by remember { mutableStateOf(0f) }
    var columnSize by remember { mutableStateOf(Size.Zero) }
    val columnHeight = with(LocalDensity.current) { columnSize.height.toDp() }
    val valuePadding = columnHeight + 24.dp

    val animFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    LaunchedEffect(key1 = value) {
        timeValue = TimeUtil.splitTime(
            seconds = value.second,
            shortForm = true,
            includeSec = viewState == GraphViewState.DAY && value.second < 60L,
            includeDays = viewState == GraphViewState.YEAR,
            includeMin = !(viewState == GraphViewState.YEAR && value.second >= 3600L)
        )
    }

    LaunchedEffect(key1 = value, key2 = maxValue) {
        targetFraction = if (maxValue > 0) {
            value.second.toFloat() / maxValue.toFloat()
        } else {
            0f
        }
    }

    LaunchedEffect(key1 = viewState) {
        targetTextRotation = when (viewState) {
            GraphViewState.WEEK, GraphViewState.YEAR -> -45f
            else -> 0f
        }
    }

    val columnWidth by animateDpAsState(
        targetValue = when {
            viewState == GraphViewState.WEEK || viewState == GraphViewState.YEAR -> 36.dp
            viewState == GraphViewState.MONTH && value.second >= 3600L -> 36.dp
            else -> 20.dp
        },
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val textRotate by animateFloatAsState(
        targetValue = targetTextRotation,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    Box(
        modifier = modifier
            .width(columnWidth)
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
            text = value.first,
            fontSize = 12.sp,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .rotate(textRotate)
        )
    }
}