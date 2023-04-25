package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.core.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedShimmer(
    colors: List<Color>? = null,
    content: @Composable (Brush) -> Unit
) {
    val finalColors = when {
        colors != null -> colors
        else -> listOf(
            MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
            MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
            MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            MaterialTheme.colors.onSurface.copy(alpha = 0.9f)
        )
    }

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = finalColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    content(brush)
}
