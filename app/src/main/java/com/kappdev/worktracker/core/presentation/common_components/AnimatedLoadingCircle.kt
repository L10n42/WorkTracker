package com.kappdev.worktracker.core.presentation.common_components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLoadingCircle(
    modifier: Modifier = Modifier,
    diameter: Dp = 32.dp,
    strokeWidth: Dp = 2.dp,
    cycleDuration: Int = 800,
    strokeColor: Color = MaterialTheme.colors.primary
) {

    val transition = rememberInfiniteTransition(label = "loading circle transition")
    val spinAngel = transition.animateFloat(
        label = "circle spin angel transition",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = cycleDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier
            .size(diameter)
            .rotate(spinAngel.value)
    ) {
        drawArc(
            color = strokeColor,
            startAngle = -70f,
            sweepAngle = 140f,
            useCenter = false,
            size = size,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = strokeColor,
            startAngle = -110f,
            sweepAngle = -140f,
            useCenter = false,
            size = size,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}












