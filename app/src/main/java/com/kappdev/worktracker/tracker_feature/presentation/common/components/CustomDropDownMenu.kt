package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kappdev.worktracker.ui.customShape

@Composable
fun CustomDropDownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    elevation: Dp = 16.dp,
    enterAnim: EnterTransition = defaultEnterAnim(),
    exitAnim: ExitTransition = defaultExitAnim(),
    offset: IntOffset = IntOffset(0, 0),
    border: BorderStroke? = null,
    alignment: Alignment = Alignment.TopEnd,
    backgroundColor: Color = MaterialTheme.colors.surface,
    shape: Shape = MaterialTheme.customShape.small,
    dismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = expanded

    if (expandedState.currentState || expandedState.targetState || !expandedState.isIdle) {
        Popup(
            onDismissRequest = dismiss,
            alignment = alignment,
            offset = offset,
            properties = PopupProperties(focusable = true)
        ) {
            AnimatedVisibility(
                visibleState = expandedState,
                enter = enterAnim,
                exit = exitAnim
            ) {
                Surface(
                    shape = shape,
                    color = backgroundColor,
                    elevation = elevation,
                    border = border,
                    modifier = modifier,
                    content = {
                        Column(content = content)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun defaultEnterAnim() = scaleIn(
    initialScale = 0.5f,
    transformOrigin = TransformOrigin(1f, 0f)
) + fadeIn()

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun defaultExitAnim() = scaleOut(
    targetScale = 0.5f,
    transformOrigin = TransformOrigin(1f, 0f)
) + fadeOut()