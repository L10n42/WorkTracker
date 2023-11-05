package com.kappdev.worktracker.tracker_feature.presentation.common.util

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.kappdev.worktracker.core.domain.util.vibrateDevice

fun Modifier.verticalDraggableInParent(): Modifier = composed {
    val context = LocalContext.current
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    var offsetYInParent by remember { mutableStateOf(0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }


    this.onGloballyPositioned { layoutCoordinates ->
        offsetX = layoutCoordinates.positionInParent().x
        offsetYInParent = layoutCoordinates.positionInParent().y

        layoutCoordinates.parentCoordinates?.let {
            parentSize = it.size
        }
    }.offset {
        IntOffset(x = offsetX.toInt(), y = offsetY.toInt())
    }.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
            onDragStart = {
                vibrateDevice(context)
            },
            onDrag = { _, dragAmount ->
                val newYOffset = offsetY + dragAmount.y

                val maxY = parentSize.height - offsetYInParent - size.height
                val minY = -offsetYInParent

                if (newYOffset > minY && newYOffset < maxY) {
                    offsetY = newYOffset
                }
            }
        )
    }
}