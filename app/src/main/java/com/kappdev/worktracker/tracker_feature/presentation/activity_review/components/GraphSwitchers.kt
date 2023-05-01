package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.customShape
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun WeekSwitcher(
    week: Pair<LocalDate, LocalDate>,
    modifier: Modifier = Modifier,
    changePeriod: (period: Pair<LocalDate, LocalDate>) -> Unit
) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(key1 = week) {
        text = "${week.first} - ${week.second}"
    }

    SwitcherBox(modifier) {
        VectorButton(
            icon = Icons.Rounded.KeyboardArrowLeft,
            onClick = { /* TODO(previous week change) */ }
        )

        SwitcherText(text)

        VectorButton(
            icon = Icons.Rounded.KeyboardArrowRight,
            onClick = { /* TODO(next week change) */ }
        )
    }
}

@Composable
fun DaySwitcher(
    date: LocalDate,
    modifier: Modifier = Modifier,
    changeDate: (date: LocalDate) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val dateDialogState = rememberMaterialDialogState()

    LaunchedEffect(key1 = date) {
        text = when (date) {
            LocalDate.now() -> "Today"
            LocalDate.now().minusDays(1) -> "Yesterday"
            else -> date.toString()
        }
    }

    DatePicker(
        initDate = date,
        setDate = changeDate,
        state = dateDialogState,
        closePicker = {
            dateDialogState.hide()
        }
    )

    SwitcherBox(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PainterButton(
                icon = painterResource(R.drawable.ic_round_double_arrow_left),
                onClick = { changeDate(date.minusDays(7)) }
            )

            VectorButton(
                icon = Icons.Rounded.KeyboardArrowLeft,
                onClick = { changeDate(date.minusDays(1)) }
            )
        }

        TextButton(
            content = { SwitcherText(text) },
            onClick = { dateDialogState.show() }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            val nextDateEnable = date < LocalDate.now()

            VectorButton(
                icon = Icons.Rounded.KeyboardArrowRight,
                enable = nextDateEnable,
                onClick = { changeDate(date.plusDays(1)) },
                tint = if (nextDateEnable) MaterialTheme.colors.onSurface else MaterialTheme.colors.onBackground
            )

            PainterButton(
                icon = painterResource(R.drawable.ic_round_double_arrow_right),
                tint = if (nextDateEnable) MaterialTheme.colors.onSurface else MaterialTheme.colors.onBackground,
                enable = nextDateEnable,
            ) {
                val newDate = date.plusDays(7)
                if (newDate < LocalDate.now()) changeDate(newDate) else changeDate(LocalDate.now())
            }
        }
    }
}

@Composable
private fun SwitcherBox(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        content = content,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
            .border(
                color = MaterialTheme.colors.onSurface,
                width = 1.dp,
                shape = MaterialTheme.customShape.medium
            )
    )
}

@Composable
private fun SwitcherText(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = MaterialTheme.colors.primary
    )
}

@Composable
private fun PainterButton(
    icon: Painter,
    tint: Color = MaterialTheme.colors.onSurface,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enable
    ) {
        Icon(
            painter = icon,
            tint = tint,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun VectorButton(
    icon: ImageVector,
    tint: Color = MaterialTheme.colors.onSurface,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enable
    ) {
        Icon(
            imageVector = icon,
            tint = tint,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun DatePicker(
    initDate: LocalDate = LocalDate.now(),
    state: MaterialDialogState,
    closePicker: () -> Unit,
    setDate: (date: LocalDate) -> Unit
) {
    var date by remember { mutableStateOf(initDate) }

    LaunchedEffect(key1 = initDate) {
        date = initDate
    }

    MaterialDialog(
        dialogState = state,
        backgroundColor = MaterialTheme.colors.surface,
        onCloseRequest = { closePicker() },
        buttons = {
            positiveButton(
                text = stringResource(R.string.btn_ok),
                textStyle = TextStyle(color = MaterialTheme.colors.primary)
            ) {
                setDate(date)
            }
            negativeButton(
                text = stringResource(R.string.btn_cancel),
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                onClick = closePicker
            )
        }
    ) {
        datepicker(
            initialDate = date,
            title = stringResource(R.string.select_date_title),
            colors = DatePickerDefaults.colors(
                calendarHeaderTextColor = MaterialTheme.colors.onSurface,
                dateInactiveTextColor = MaterialTheme.colors.onSurface,
            ),
            allowedDateValidator = { localDate ->
                localDate <= LocalDate.now()
            }
        ) {
            date = it
        }
    }
}