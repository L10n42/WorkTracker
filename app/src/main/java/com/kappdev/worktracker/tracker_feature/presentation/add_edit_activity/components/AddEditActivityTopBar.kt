package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivityViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.theme.SuperDarkGray

@Composable
fun AddEditActivityTopBar(
    viewModel: AddEditActivityViewModel,
    onBack: () -> Unit
) {
    val title = viewModel.activity.value.name
    TopAppBar(
        backgroundColor = SuperDarkGray,
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(
                value = if (title.isEmpty()) {
                    stringResource(R.string.create_activity_title)
                } else {
                    stringResource(R.string.edit_activity_title)
                }
            )
        },
        navigationIcon = {
            BackButton(onClick = onBack)
        }
    )
}