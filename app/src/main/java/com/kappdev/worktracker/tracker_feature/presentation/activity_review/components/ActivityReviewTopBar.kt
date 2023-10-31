package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.theme.getTopBarColor

@Composable
fun ActivityReviewTopBar(
    activity: Activity?,
    onBack: () -> Unit,
    onNavigate: (route: String) -> Unit
) {
    TopAppBar(
        backgroundColor = getTopBarColor(),
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(activity?.name ?: "")
        },
        navigationIcon = {
            BackButton(onClick = onBack)
        },
        actions = {
            IconButton(
                onClick = {
                    activity?.let {
                        onNavigate(Screen.AddEditActivity.route + "?activityId=${activity.id}")
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "go to edit activity button",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}