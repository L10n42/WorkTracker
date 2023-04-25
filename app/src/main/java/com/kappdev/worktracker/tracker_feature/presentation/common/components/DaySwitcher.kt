package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import java.time.LocalDate

@Composable
fun DaySwitcher(
    date: LocalDate,
    modifier: Modifier = Modifier,
    changeDate: (date: LocalDate) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = date) {
        text = when (date) {
            LocalDate.now() -> "Today"
            LocalDate.now().minusDays(1) -> "Yesterday"
            else -> date.toString()
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    changeDate(date.minusDays(7))
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_double_arrow_left),
                    contentDescription = "back few date",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = {
                    changeDate(date.minusDays(1))
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "back date",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        TextButton(
            onClick = {

            }
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                color = MaterialTheme.colors.primary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val nextDateEnable = date < LocalDate.now()
            IconButton(
                enabled = nextDateEnable,
                onClick = {
                    changeDate(date.plusDays(1))
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = "next date",
                    tint = if (nextDateEnable) MaterialTheme.colors.onSurface else MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                enabled = nextDateEnable,
                onClick = {
                    val newDate = date.plusDays(7)
                    if (newDate > date) changeDate(LocalDate.now()) else changeDate(newDate)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_double_arrow_right),
                    contentDescription = "next few date",
                    tint = if (nextDateEnable) MaterialTheme.colors.onSurface else MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}