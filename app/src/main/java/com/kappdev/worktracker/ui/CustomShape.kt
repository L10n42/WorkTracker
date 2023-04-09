package com.kappdev.worktracker.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class CustomShape(
    val extraSmall: Shape = RoundedCornerShape(2.dp),
    val small: Shape = RoundedCornerShape(4.dp),
    val medium: Shape = RoundedCornerShape(8.dp),
    val large: Shape = RoundedCornerShape(16.dp),
    val extraLarge: Shape = RoundedCornerShape(32.dp)
)

val LocalCustomShape = compositionLocalOf { CustomShape() }

val MaterialTheme.customShape: CustomShape
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomShape.current