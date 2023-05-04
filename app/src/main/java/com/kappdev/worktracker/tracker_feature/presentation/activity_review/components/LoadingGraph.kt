package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedShimmer
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing
import kotlin.random.Random

@Composable
fun LoadingGraph(
    modifier: Modifier = Modifier
) {
    AnimatedShimmer { shimmerBrush ->
        Column(
            modifier = modifier
                .border(
                    color = MaterialTheme.colors.onSurface,
                    width = 1.dp,
                    shape = MaterialTheme.customShape.medium
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxHeight(0.85f)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(24) { item ->
                        AnimatedColumn(brush = shimmerBrush, columnNumber = item)
                    }
                }

                Spacer(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 19.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.onBackground)
                )
            }

            Spacer(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .height(24.dp)
                    .width(170.dp)
                    .background(
                        brush = shimmerBrush,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun AnimatedColumn(
    brush: Brush,
    columnNumber: Int
) {
    fun getRandom() = Random.nextInt(1, 100) / 100f
    var targetFraction by remember { mutableStateOf(getRandom()) }

    val animFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 400, easing = LinearEasing)
    )

    if (animFraction == targetFraction) {
        targetFraction = getRandom()
    }

    Box(
        modifier = Modifier
            .width(16.dp)
            .fillMaxHeight()
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp, top = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(animFraction)
                .background(
                    brush = brush,
                    shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                )
        )

        Text(
            text = columnNumber.toString().padStart(2,'0'),
            fontSize = 12.sp,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}