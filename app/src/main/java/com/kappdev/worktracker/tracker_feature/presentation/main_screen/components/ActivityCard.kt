package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActivityCard(
    activity: Activity,
    viewModel: MainScreenViewModel,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    isStopwatchActive: Boolean,
    isCountdownActive: Boolean,
    onNavigate: (route: String) -> Unit,
    onStart: () -> Unit,
    onStartTimer: () -> Unit
) {
    val animatedSpace by animateDpAsState(
        targetValue = if (isSelectionMode) 40.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "space"
    )

    val animateScale by animateFloatAsState(
        targetValue = if (isSelectionMode) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "scale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.extraSmall)
            .shadow(elevation = MaterialTheme.elevation.extraSmall)
            .background(MaterialTheme.colors.surface)
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        if (isSelected) viewModel.deselect(activity) else viewModel.select(activity)
                    } else {
                        onNavigate(Screen.ActivityReview.route + "?activityId=${activity.id}")
                    }
                },
                onLongClick = {
                    if (!isSelectionMode) {
                        viewModel.switchSelectionModeOn()
                        viewModel.select(activity)
                    }
                }
            )
    ) {
        Box(
            modifier = Modifier.width(animatedSpace),
            contentAlignment = Alignment.Center
        ) {
            val icon = when (isSelected) {
                true -> painterResource(R.drawable.ic_round_done_black_bold)
                false -> painterResource(R.drawable.ic_round_close_black_bold)
            }
            val iconTint = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
            Icon(
                painter = icon,
                tint = iconTint,
                contentDescription = "selection indicator",
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.small)
                    .size(18.dp)
                    .scale(animateScale)
            )
        }

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

        IconButton(
            onClick = onStartTimer,
            enabled = !isSelectionMode
        ) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = "button to start timer for current activity",
                tint = if (isCountdownActive) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
            )
        }

        IconButton(
            onClick = onStart,
            enabled = !isSelectionMode
        ) {
            Icon(
                imageVector = if (isStopwatchActive) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = "arrow to start tracking the time",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}