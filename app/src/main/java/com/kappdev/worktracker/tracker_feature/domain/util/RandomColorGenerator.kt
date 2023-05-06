package com.kappdev.worktracker.tracker_feature.domain.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import kotlin.random.Random

object RandomColorGenerator {

    fun getBrightColor(): Color {
        val random = Random.Default
        val hue = random.nextInt(256).toFloat()
        val saturation = 1f
        val lightness = 0.5f

        val color = ColorUtils.HSLToColor(floatArrayOf(hue, saturation, lightness))
        return Color(color)
    }
}