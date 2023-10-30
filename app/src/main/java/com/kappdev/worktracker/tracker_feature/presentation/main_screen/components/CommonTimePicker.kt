package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.domain.model.Time
import com.kappdev.worktracker.tracker_feature.domain.model.stringFormat
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.CommonDurations
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing
import com.kappdev.worktracker.ui.theme.getButtonColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CommonTimePicker(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onTimePicked: (time: Time) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = 400,
                easing = LinearEasing
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 300,
                easing = LinearEasing
            )
        ),
        exit = scaleOut(
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = 100,
                easing = LinearEasing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            )
        )
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            userScrollEnabled = false,
            columns = GridCells.Fixed(3)
        ) {
            items(CommonDurations.list) { time ->
                CommonTimeButton(
                    title = time.stringFormat(),
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.70f)
                        .padding(MaterialTheme.spacing.small),
                    onClick = { onTimePicked(time) }
                )
            }
        }
    }
}

@Composable
private fun CommonTimeButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.customShape.medium,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = getButtonColor()
        )
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colors.primary
        )
    }
}