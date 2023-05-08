package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun InfoButton(
    text: String,
    modifier: Modifier = Modifier,
    width: Dp = 240.dp,
    height: Dp = 75.dp,
    icon: ImageVector = Icons.Default.Info
) {
    Box(
        modifier = modifier.wrapContentSize()
    ) {
        var iconSize by remember { mutableStateOf(Size.Zero) }
        val offsetX = -(iconSize.height.toInt())
        val offsetY = -(iconSize.height * 1.5).toInt()

        var showPopup by remember { mutableStateOf(false) }
        MessagePopup(
            expanded = showPopup,
            message = text,
            width = width,
            height = height,
            offset = IntOffset(offsetX, offsetY),
            closePopup = { showPopup = false },
        )

        IconButton(
            onClick = {
                showPopup = true
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "info button",
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    iconSize = coordinates.size.toSize()
                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MessagePopup(
    message: String,
    expanded: Boolean,
    height: Dp,
    width: Dp,
    alignment: Alignment = Alignment.BottomEnd,
    offset: IntOffset = IntOffset(0, 0),
    closePopup: () -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    expandedState.targetState = expanded

    if (expandedState.currentState || expandedState.targetState || !expandedState.isIdle) {
        Popup(
            alignment = alignment,
            offset = offset,
            onDismissRequest = closePopup,
            properties = PopupProperties(focusable = true)
        ) {
            AnimatedVisibility(
                visibleState = expandedState,
                enter = scaleIn(
                    transformOrigin = TransformOrigin(1f, 1f)
                ) + fadeIn(),
                exit = scaleOut(
                    transformOrigin = TransformOrigin(1f, 1f)
                ) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .height(height)
                        .width(width)
                        .shadow(elevation = 8.dp)
                ) {
                    MessageForm(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.surface
                    )

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(bottom = height * 0.3f)
                            .padding(all = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageForm(
    modifier: Modifier = Modifier,
    color: Color
) {
    Canvas(
        modifier = modifier,
    ) {
        val path = Path().apply {
            moveTo(size.width * 0.85f, size.height * 0.7f)
            lineTo(size.width * 0.95f, size.height)
            lineTo(size.width * 0.93f, size.height * 0.7f)
            close()
        }

        drawPath(
            path = path,
            color = color,
        )

        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
            size = Size(size.width, size.height * 0.71f)
        )
    }
}

























