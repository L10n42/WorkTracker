package com.kappdev.worktracker.tracker_feature.presentation.countdonw_timer.componets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.data.service.countdown.CountdownService
import com.kappdev.worktracker.tracker_feature.domain.model.stringFormat
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedTimer
import com.kappdev.worktracker.tracker_feature.presentation.common.util.TimerAnimationDirection
import com.kappdev.worktracker.ui.spacing
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LargeCountdownTimer(
    modifier: Modifier = Modifier,
    countdownService: CountdownService,
    inactiveBarColor: Color = MaterialTheme.colors.onBackground,
    activeBarColor: Color = MaterialTheme.colors.primary,
    thumbColor: Color = MaterialTheme.colors.primary
) {
    val time by countdownService.time
    val totalTime by countdownService.totalTime
    val completionPercentage by countdownService.completionPercentage

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -90f,
                sweepAngle = -360f,
                useCenter = false,
                size = size,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Square)
            )

            drawArc(
                color = activeBarColor,
                startAngle = -90f,
                sweepAngle = -360f * completionPercentage,
                useCenter = false,
                size = size,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Square)
            )

            val corner = (-360f * completionPercentage - 90f) * (PI / 180f).toFloat()
            val radius = this.size.width / 2
            val thumbX = radius * cos(corner)
            val thumbY = radius * sin(corner)

            drawPoints(
                points = listOf(Offset(center.x + thumbX, center.y + thumbY)),
                color = thumbColor,
                strokeWidth = 8.dp.toPx(),
                pointMode = PointMode.Points,
                cap = StrokeCap.Round
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedTimer(
                time = time,
                style = TextStyle(
                    fontSize = 42.sp,
                    color = MaterialTheme.colors.onSurface
                ),
                direction = TimerAnimationDirection.Bottom
            )

            val totalString = stringResource(R.string.total) + " " + totalTime.stringFormat()
            Text(
                text = totalString,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
            )
        }
    }
}