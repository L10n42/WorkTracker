package com.kappdev.worktracker.tracker_feature.domain.util

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

object ColorUtil {

    fun getColorDistance(color1: Color, color2: Color): Double {
        val lab1 = color1.toLAB()
        val lab2 = color2.toLAB()

        val deltaL = lab1.l - lab2.l
        val deltaA = lab1.a - lab2.a
        val deltaB = lab1.b - lab2.b

        return sqrt(deltaL * deltaL + deltaA * deltaA + deltaB * deltaB)
    }

    private fun Color.toLAB(): LAB {
        val x = 0.412453 * red + 0.357580 * green + 0.180423 * blue
        val y = 0.212671 * red + 0.715160 * green + 0.072169 * blue
        val z = 0.019334 * red + 0.119193 * green + 0.950227 * blue

        val refX = 0.95047
        val refY = 1.0
        val refZ = 1.08883

        val xNorm = x / refX
        val yNorm = y / refY
        val zNorm = z / refZ

        val fx = if (xNorm > 0.008856) {
            xNorm.pow(1.0 / 3.0)
        } else {
            7.787 * xNorm + 16.0 / 116.0
        }

        val fy = if (yNorm > 0.008856) {
            yNorm.pow(1.0 / 3.0)
        } else {
            7.787 * yNorm + 16.0 / 116.0
        }

        val fz = if (zNorm > 0.008856) {
            zNorm.pow(1.0 / 3.0)
        } else {
            7.787 * zNorm + 16.0 / 116.0
        }

        val l = 116.0 * fy - 16.0
        val a = 500.0 * (fx - fy)
        val b = 200.0 * (fy - fz)

        return LAB(l, a, b)
    }

    private data class LAB(val l: Double, val a: Double, val b: Double)
}