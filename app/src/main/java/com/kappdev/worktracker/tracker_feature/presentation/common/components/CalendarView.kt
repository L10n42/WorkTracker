package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing
import com.kappdev.worktracker.ui.theme.*
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalendarView(
    date: LocalDate,
    data: Map<Int, Long>,
    target: Long,
    modifier: Modifier = Modifier,
    changeDate: (date: LocalDate) -> Unit
) {
    val dayOfWeeks = stringArrayResource(R.array.week_days)
    val gradeState = rememberLazyGridState()
    val largeCalendar by remember {
        derivedStateOf {
            gradeState.layoutInfo.totalItemsCount > 35
        }
    }

    val animatedHeight by animateDpAsState(
        targetValue = if (largeCalendar) 410.dp else 365.dp,
        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
    )

    Column(
        modifier = modifier
            .height(animatedHeight)
            .border(
                color = MaterialTheme.colors.onSurface,
                width = 1.dp,
                shape = MaterialTheme.customShape.medium
            )
    ) {
        StatePanel(
            date = date,
            modifier = Modifier.fillMaxWidth(),
            changeDate = changeDate
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            userScrollEnabled = false
        ) {
            items(dayOfWeeks) { dayOfWeek ->
                Text(
                    text = dayOfWeek,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        AnimatedContent(
            targetState = date,
            transitionSpec = {
                if (this.targetState > this.initialState) {
                    slideInHorizontally { it } + fadeIn() with slideOutHorizontally { -it } + fadeOut()
                } else {
                    slideInHorizontally { -it } + fadeIn() with slideOutHorizontally { it } + fadeOut()
                }
            }
        ) { state ->
            val start = state.withDayOfMonth(1)
            val startWeekDay = start.dayOfWeek.value
            val days = DateUtil.getMonthDays(state)

            LazyVerticalGrid(
                state = gradeState,
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                userScrollEnabled = false
            ) {
                items(startWeekDay - 1) {
                    Spacer(modifier = Modifier.aspectRatio(1f))
                }

                items(days) { day ->
                    DateCard(
                        day = day,
                        time = data[day] ?: 0L,
                        target = target,
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                    )
                }
            }
        }

        GradesScale(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun GradesScale(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        GradeTitle(R.string.title_less)

        HeatScaleColors.forEach { color ->
            GradeCard(color)
        }

        GradeTitle(R.string.title_more)
    }
}

@Composable
fun GradeTitle(
    resId: Int
) {
    Text(
        text = stringResource(resId),
        fontSize = 16.sp,
        color = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun GradeCard(color: Color) {
    Spacer(
        Modifier
            .size(24.dp)
            .background(
                color = color,
                shape = MaterialTheme.customShape.small
            )
    )
}

@Composable
private fun StatePanel(
    date: LocalDate,
    modifier: Modifier = Modifier,
    changeDate: (date: LocalDate) -> Unit
) {
    val month = date.month.name.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val year = date.year

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(
            onClick = {
                changeDate(date.minusMonths(1))
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "previous month",
                tint = MaterialTheme.colors.onSurface
            )
        }

        Text(
            text = "$month $year",
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface
        )

        IconButton(
            onClick = {
                changeDate(date.plusMonths(1))
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = "next month",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun DateCard(
    day: Int,
    time: Long,
    target: Long,
    modifier: Modifier = Modifier
) {
    val color = when {
        time > target -> Completed100
        time > (target * 0.75f) -> Completed75
        time > (target * 0.5f) -> Completed50
        time > (target * 0.3f) -> Completed30
        time > 0 -> Completed10
        else -> MaterialTheme.colors.surface
    }
    Box(
        modifier = modifier
            .background(
                color = color,
                shape = MaterialTheme.customShape.small
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}