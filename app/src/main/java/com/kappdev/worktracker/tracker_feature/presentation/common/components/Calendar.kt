package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.util.DateUtil
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Calendar(
    date: LocalDate,
    data: Map<Int, Long>,
    modifier: Modifier = Modifier,
    changeDate: (date: LocalDate) -> Unit
) {
    val dayOfWeeks = stringArrayResource(R.array.week_days)

    Column(
        modifier = modifier.border(
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
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
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
    modifier: Modifier = Modifier
) {
    val color = when {
        time > 60L -> MaterialTheme.colors.primary
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