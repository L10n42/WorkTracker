package com.kappdev.worktracker.tracker_feature.presentation.common.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

data class CalendarDay(val date: LocalDate, val isCurrentMonth: Boolean)

data class CalendarMonth(val month: YearMonth, val days: List<CalendarDay>)

@Composable
fun Calendar(
    modifier: Modifier = Modifier
) {
    val dayOfWeeks = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var currantDate by remember { mutableStateOf(LocalDate.now()) }
    val start = currantDate.withDayOfMonth(1)
    val startWeekDay = start.dayOfWeek.value
    val days = monthDays(currantDate)

    Column(
        modifier = modifier.border(
            color = MaterialTheme.colors.onSurface,
            width = 1.dp,
            shape = MaterialTheme.customShape.medium
        )
    ) {
        StatePanel(
            date = currantDate,
            modifier = Modifier.fillMaxWidth(),
            changeDate = {
                currantDate = it
            }
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

            items(startWeekDay - 1) {
                Spacer(modifier = Modifier.aspectRatio(1f))
            }

            items(days) { day ->
                DateCard(
                    day = day,
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

private fun monthDays(date: LocalDate): List<Int> {
    val start = date.withDayOfMonth(1)
    val daysInMonth = date.month.length(date.isLeapYear)
    return (0 until daysInMonth).map { start.plusDays(it.toLong()).dayOfMonth }
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.surface,
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