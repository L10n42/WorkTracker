package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.kappdev.worktracker.ui.spacing

@Composable
fun CustomDailyGraph(
    value: Map<Int, Long>,
    modifier: Modifier = Modifier,
    maxValue: Long = value.maxByOrNull { it.value }?.value ?: 0L,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small)
                .fillMaxHeight(0.9f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                value.forEach { map ->
                    GraphColumn(map, maxValue)
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

        // TODO(implement here total time info bar)
    }
}

@Composable
fun GraphColumn(
    map: Map.Entry<Int, Long>,
    maxValue: Long,
    modifier: Modifier = Modifier
) {
    var value by remember { mutableStateOf("") }

    var targetFraction by remember { mutableStateOf(0f) }
    var columnSize by remember { mutableStateOf(Size.Zero) }
    val columnHeight = with(LocalDensity.current) { columnSize.height.toDp() }
    val valuePadding = columnHeight + 24.dp

    val animFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    LaunchedEffect(key1 = map) {
        val time = map.value / 60f
        value = when {
            time > 0 && time < 1 -> "${map.value}s"
            time > 1 -> "${time.toInt()}m"
            else -> ""
        }
    }

    LaunchedEffect(key1 = map, key2 = maxValue) {
        targetFraction = (map.value / 60).toFloat() / (maxValue / 60).toFloat()
    }

    Box(
        modifier = modifier
            .width(16.dp)
            .fillMaxHeight()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animFraction)
                .padding(bottom = 20.dp, top = 16.dp)
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                )
                .onGloballyPositioned { coordinates ->
                    columnSize = coordinates.size.toSize()
                }
        )


        Text(
            text = value,
            fontSize = 8.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = valuePadding)
        )

        Text(
            text = map.key.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}