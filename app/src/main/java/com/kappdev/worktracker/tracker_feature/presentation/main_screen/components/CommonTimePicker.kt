package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.domain.model.Time
import com.kappdev.worktracker.tracker_feature.domain.model.stringFormat
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CommonTimePicker(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onTimePicked: (time: Time) -> Unit
) {
    val commonTime = listOf(
        Time(minutes = "05"),
        Time(minutes = "10"),
        Time(minutes = "15"),
        Time(minutes = "30"),
        Time(minutes = "45"),
        Time(hours = "01"),
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            initialScale = 0.5f
        ) + fadeIn(),
        exit = scaleOut(
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            targetScale = 0.5f
        ) + fadeOut()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            modifier = modifier,
            userScrollEnabled = false
        ) {
            items(commonTime) { time ->
                CommonTimeButton(
                    title = time.stringFormat(),
                    modifier = Modifier
                        .wrapContentSize()
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
        shape = MaterialTheme.customShape.large,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface.copy(0.64f)
        )
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colors.primary
        )
    }
}