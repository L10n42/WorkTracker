package com.kappdev.worktracker.tracker_feature.presentation.common.components

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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.spacing

@Composable
fun TimerPicker(
    modifier: Modifier,
    onValueChange: (timeInMillis: Long) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var hours by remember { mutableStateOf(0) }
        var minutes by remember { mutableStateOf(0) }
        var seconds by remember { mutableStateOf(0) }

        LaunchedEffect(key1 = hours, key2 = minutes, key3 = seconds) {
            onValueChange(
                ((hours * 3600) + (minutes * 60) + seconds) * 1000L
            )
        }

        NumberPicker(
            value = hours,
            onValueChange = { hours = it }
        )

        TimerSeparator()

        NumberPicker(
            value = minutes,
            onValueChange = { minutes = it }
        )

        TimerSeparator()

        NumberPicker(
            value = seconds,
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
    value: Int,
    onValueChange: (value: Int) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.number_picker, null)
            val numberPicker = view.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 59
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
