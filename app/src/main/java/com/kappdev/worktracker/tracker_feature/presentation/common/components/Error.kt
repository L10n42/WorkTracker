package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.ui.customShape
import com.kappdev.worktracker.ui.spacing
import com.kappdev.worktracker.ui.theme.StrongRed
import com.kappdev.worktracker.ui.theme.LightRed

@Composable
fun Error(
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val customModifier = Modifier.background(
        color = LightRed,
        shape = MaterialTheme.customShape.medium
    )

    Row(
        modifier = modifier.then(customModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ErrorIcon(
            modifier = Modifier.padding(all = MaterialTheme.spacing.small)
        )

        ErrorMassage(
            message = message,
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.medium)
                .weight(1f)
        )

        DismissButton(
            onDismiss = onDismiss,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}

@Composable
private fun ErrorIcon(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.then(
            Modifier
                .size(32.dp)
                .background(
                    color = StrongRed,
                    shape = CircleShape
                )
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.PriorityHigh,
            contentDescription = "error icon",
            tint = Color.White
        )
    }
}

@Composable
private fun ErrorMassage(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        color = StrongRed,
        fontSize = 16.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        modifier = modifier
    )
}

@Composable
private fun DismissButton(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    IconButton(
        onClick = onDismiss,
        modifier = Modifier
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "dismiss error button",
            tint = Color.White,
            modifier = modifier.size(18.dp)
        )
    }
}
