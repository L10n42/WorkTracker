package com.kappdev.worktracker.core.presentation.common_components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedDotsText(
    text: String,
    modifier: Modifier = Modifier,
    cycleDuration: Int = 800,
    style: TextStyle = TextStyle(fontSize = 18.sp)
) {
    val transition = rememberInfiniteTransition(label = "dots transition")
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
        ),
        label = "dots amount transition"
    )

    Text(
        text = text + ".".repeat(dotsVisible.value),
        modifier = modifier,
        style = style
    )
}



















