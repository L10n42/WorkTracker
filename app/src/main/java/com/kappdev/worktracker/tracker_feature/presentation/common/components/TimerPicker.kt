package com.kappdev.worktracker.tracker_feature.presentation.common.components

import android.graphics.Color.toArgb
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.domain.model.Time
import com.kappdev.worktracker.tracker_feature.domain.model.getHours
import com.kappdev.worktracker.tracker_feature.domain.model.getMinutes
import com.kappdev.worktracker.tracker_feature.domain.model.getSeconds
import com.kappdev.worktracker.ui.spacing

@Composable
fun TimerPicker(
    modifier: Modifier,
    defaultValue: Time = Time(),
    onValueChange: (time: Time) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var hours by remember { mutableStateOf(0) }
        var minutes by remember { mutableStateOf(0) }
        var seconds by remember { mutableStateOf(0) }

        LaunchedEffect(key1 = defaultValue) {
            hours = defaultValue.getHours()
            minutes = defaultValue.getMinutes()
            seconds = defaultValue.getSeconds()
        }

        LaunchedEffect(key1 = hours, key2 = minutes, key3 = seconds) {
            onValueChange(
                Time.from(hours, minutes, seconds)
            )
        }

        NumberPicker(
            timerValue = hours,
            max = 23,
            onValueChange = { hours = it }
        )

        TimerSeparator()

        NumberPicker(
            timerValue = minutes,
            onValueChange = { minutes = it }
        )

        TimerSeparator()

        NumberPicker(
            timerValue = seconds,
            onValueChange = { seconds = it }
        )
    }
}

@Composable
private fun TimerSeparator() {
    HorizontalSpace(space = MaterialTheme.spacing.small)
    Text(text = ":", fontSize = 48.sp, color = Color.White)
    HorizontalSpace(space = MaterialTheme.spacing.small)
}

@Composable
private fun NumberPicker(
    modifier: Modifier = Modifier,
    timerValue: Int,
    min: Int = 0,
    max: Int = 59,
    onValueChange: (value: Int) -> Unit
) {
    var value by remember { mutableStateOf(timerValue) }
    LaunchedEffect(key1 = timerValue) {
        value = timerValue
    }

    val backgroundColor = MaterialTheme.colors.background.toArgb()
    AndroidView(
        modifier = modifier,
        update = { it.value = value },
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.number_picker, null)
            val numberPicker = view.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.setBackgroundColor(backgroundColor)
            numberPicker.clipToOutline = true
            numberPicker.minValue = min
            numberPicker.maxValue = max
            numberPicker.value = value
            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                onValueChange(newVal)
            }
            numberPicker.setFormatter { value ->
                value.toString().padStart(2, '0')
            }
            numberPicker
        }
    )
}
