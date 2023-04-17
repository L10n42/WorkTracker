package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedDigit(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1
) {
    var oldCount by remember { mutableStateOf(count) }

    SideEffect {
        oldCount = count
    }

    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCount.toString()
        for (i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if (oldChar == newChar) oldCountString[i] else countString[i]

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically {
                        if (oldChar != null) {
                            if (oldChar.digitToInt() > newChar.digitToInt()) -it else it
                        } else it
                    } with slideOutVertically {
                        if (oldChar != null) {
                            if (oldChar.digitToInt() > newChar.digitToInt()) it else -it
                        } else -it
                    }
                }
            ) { animatedChar ->
                Text(
                    text = animatedChar.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}