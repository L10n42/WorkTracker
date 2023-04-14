package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.spacing

@Composable
fun ActivityCard(
    activity: Activity,
    isStopwatchActive: Boolean,
    isCountdownActive: Boolean,
    onStart: () -> Unit,
    onStartTimer: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.extraSmall)
            .shadow(elevation = MaterialTheme.elevation.extraSmall)
            .background(MaterialTheme.colors.surface)
            .clickable {

            }
    ) {
        Text(
            text = activity.name,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.small)
                .weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(onClick = onStartTimer) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = "button to start timer for current activity",
                tint = if (isCountdownActive) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
            )
        }

        IconButton(onClick = onStart) {
            Icon(
                imageVector = if (isStopwatchActive) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "arrow to start tracking the time",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}