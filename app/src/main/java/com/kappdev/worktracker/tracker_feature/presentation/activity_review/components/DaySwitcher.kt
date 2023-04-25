package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.foundation.border
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

    Row(
        modifier = modifier
            .border(
                color = MaterialTheme.colors.onSurface,
                width = 1.dp,
                shape = MaterialTheme.customShape.medium
            ),
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
                dateDialogState.show()
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
                    if (newDate < LocalDate.now()) changeDate(newDate) else changeDate(LocalDate.now())
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