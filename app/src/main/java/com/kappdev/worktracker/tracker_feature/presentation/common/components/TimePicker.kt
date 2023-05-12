package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.kappdev.worktracker.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun TimePicker(
    initTime: LocalTime = LocalTime.now(),
    state: MaterialDialogState,
    label: String = "",
    closePicker: () -> Unit,
    setTime: (time: LocalTime) -> Unit
) {
    var time by remember {
        mutableStateOf(initTime)
    }

    LaunchedEffect(key1 = initTime) {
        time = initTime
    }

    MaterialDialog(
        dialogState = state,
        backgroundColor = MaterialTheme.colors.surface,
        onCloseRequest = { closePicker() },
        buttons = {
            positiveButton(
                text = stringResource(R.string.btn_ok),
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                onClick = { setTime(time) }
            )
            negativeButton(
                text = stringResource(R.string.btn_cancel),
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                onClick = closePicker
            )
        }
    ) {
        timepicker(
            initialTime = time,
            title = label,
            is24HourClock = true,
            onTimeChange = { newTime ->
                time = newTime
            },
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colors.primary,
                activeTextColor = MaterialTheme.colors.onSurface,
                inactiveTextColor = MaterialTheme.colors.onSurface,
                headerTextColor = MaterialTheme.colors.onSurface,
                selectorColor = MaterialTheme.colors.primary,
                borderColor = MaterialTheme.colors.onBackground
            )
        )
    }
}