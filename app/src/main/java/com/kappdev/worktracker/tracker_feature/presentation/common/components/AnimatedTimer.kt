package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.kappdev.worktracker.tracker_feature.domain.model.Time
import com.kappdev.worktracker.tracker_feature.presentation.common.util.TimerAnimationDirection

@Composable
fun AnimatedTimer(
    time: Time,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1,
    direction: TimerAnimationDirection = TimerAnimationDirection.Top
) {
    val hours = time.hours
    val minutes = time.minutes
    val seconds = time.seconds

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AnimatedTime(hours, style, direction)

        Separator(style)

        AnimatedTime(minutes, style, direction)

        Separator(style)

        AnimatedTime(seconds, style, direction)
    }
}

@Composable
private fun Separator(style: TextStyle) {
    Text(text = ":", style = style)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedTime(
    digit: String,
    style: TextStyle,
    direction: TimerAnimationDirection
) {
    var oldDigit by remember { mutableStateOf(digit) }
    SideEffect { oldDigit = digit }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        for (i in digit.indices) {
            val oldChar = oldDigit.getOrNull(i)
            val newChar = digit[i]
            val char = if (newChar == oldChar) oldDigit[i] else digit[i]

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically {
                        when (direction) {
                            TimerAnimationDirection.Top -> it
                            TimerAnimationDirection.Bottom -> -it
                        }
                    } with slideOutVertically {
                        when (direction) {
                            TimerAnimationDirection.Top -> -it
                            TimerAnimationDirection.Bottom -> it
                        }
                    }
                }
            ) { animChar ->
                Text(
                    text = animChar.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}