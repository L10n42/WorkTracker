package com.kappdev.worktracker.core.presentation.common_components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.ui.theme.WorkTrackerTheme

@Composable
fun AnimatedDotsText(
    text: String,
    modifier: Modifier = Modifier,
    cycleDuration: Int = 1_200,
    style: TextStyle = TextStyle(fontSize = 18.sp)
) {
    val transition = rememberInfiniteTransition()
    val dotsVisible = transition.animateValue(
        initialValue = 1,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Text(
        text = text + ".".repeat(dotsVisible.value),
        modifier = modifier,
        style = style
    )
}



















