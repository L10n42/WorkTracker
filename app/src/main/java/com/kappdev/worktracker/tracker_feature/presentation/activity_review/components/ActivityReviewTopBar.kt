package com.kappdev.worktracker.tracker_feature.presentation.activity_review.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.activity_review.ActivityReviewViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.ui.elevation

@Composable
fun ActivityReviewTopBar(
    viewModel: ActivityReviewViewModel
) {
    val activityName = viewModel.currentActivity.value?.name
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(activityName ?: "")
        },
        navigationIcon = {
            BackButton {
                viewModel.navigate(Screen.Main.route)
            }
        },
        actions = {
            IconButton(onClick = viewModel::gotoEdit) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "go to edit activity button",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}