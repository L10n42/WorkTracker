package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivityViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.ui.elevation

@Composable
fun AddEditActivityTopBar(
    viewModel: AddEditActivityViewModel
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(R.string.add_edit_screen_title)
        },
        navigationIcon = {
            BackButton {
                viewModel.navigate(Screen.Main.route)
            }
        },
        actions = {
            IconButton(
                onClick = {
                    if (viewModel.canSave()) {
                        viewModel.save()
                        viewModel.navigate(Screen.Main.route)
                    } else {
                        viewModel.showError(R.string.unfilled_field_error)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "save activity button",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}