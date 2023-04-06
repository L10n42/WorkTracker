package com.kappdev.worktracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PrimaryBlue,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    background = DarkGray,
    surface = LightGray,
    error = ErrorRed,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = onBackgroundDark,
    onSurface = onSurfaceDark,
    onError = Color.Black
)

private val LightColorPalette = lightColors(
    primary = PrimaryBlue,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal700,
    background = SuperLightGray,
    surface = Color.White,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = onBackgroundLight,
    onSurface = onSurfaceLight,
    onError = Color.White
)

@Composable
fun WorkTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}