package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.util.getMonthPeriod
import com.kappdev.worktracker.tracker_feature.domain.util.getMonthToDisplay
import com.kappdev.worktracker.tracker_feature.domain.util.getWeek
import com.kappdev.worktracker.tracker_feature.domain.util.getWeekToDisplay
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.GraphViewState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun PeriodSwitcher(
    date: LocalDate,
    viewState: GraphViewState,
    modifier: Modifier = Modifier,
    changeDate: (date: LocalDate) -> Unit
) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(key1 = date) {
        text = when (viewState) {
            GraphViewState.YEAR -> date.year.toString()
            GraphViewState.MONTH -> date.getMonthToDisplay()
            GraphViewState.WEEK -> date.getWeekToDisplay()
            else -> ""
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VectorButton(
            icon = Icons.Rounded.KeyboardArrowLeft,
            onClick = {
                val newDate = when (viewState) {
                    GraphViewState.YEAR -> date.minusYears(1)
                    GraphViewState.MONTH -> date.minusMonths(1)
                    GraphViewState.WEEK -> date.minusWeeks(1)
                    else -> date
                }
                changeDate(newDate)
            }
        )

        SwitcherText(text)

        val nextEnable = when(viewState) {
            GraphViewState.YEAR -> date.plusYears(1).year <= LocalDate.now().year
            GraphViewState.MONTH -> date.plusMonths(1) < LocalDate.now().getMonthPeriod().second
            GraphViewState.WEEK -> date.plusWeeks(1) < LocalDate.now().getWeek().second
            else -> true
        }

        VectorButton(
            icon = Icons.Rounded.KeyboardArrowRight,
            enable = nextEnable,
            onClick = {
                val newDate = when (viewState) {
                    GraphViewState.YEAR -> date.plusYears(1)
                    GraphViewState.MONTH -> date.plusMonths(1)
                    GraphViewState.WEEK -> date.plusWeeks(1)
                    else -> date
                }
                changeDate(newDate)
            }
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

    LaunchedEffect(date) {
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
        closePicker = dateDialogState::hide
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
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

        TextButton(onClick = dateDialogState::show) {
            SwitcherText(text, TextDecoration.Underline)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            val nextDateEnable = date < LocalDate.now()

            VectorButton(
                icon = Icons.Rounded.KeyboardArrowRight,
                enable = nextDateEnable,
                onClick = { changeDate(date.plusDays(1)) }
            )

            PainterButton(
                icon = painterResource(R.drawable.ic_round_double_arrow_right),
                enable = nextDateEnable
            ) {
                val newDate = date.plusDays(7)
                if (newDate < LocalDate.now()) changeDate(newDate) else changeDate(LocalDate.now())
            }
        }
    }
}

@Composable
private fun SwitcherText(
    text: String,
    textDecoration: TextDecoration? = null
) {
    Text(
        text = text,
        fontSize = 18.sp,
        color = MaterialTheme.colors.primary,
        textDecoration = textDecoration
    )
}

@Composable
private fun PainterButton(
    icon: Painter,
    enable: Boolean = true,
    tint: Color = if (enable) MaterialTheme.colors.onSurface else MaterialTheme.colors.onBackground,
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
    enable: Boolean = true,
    tint: Color = if (enable) MaterialTheme.colors.onSurface else MaterialTheme.colors.onBackground,
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