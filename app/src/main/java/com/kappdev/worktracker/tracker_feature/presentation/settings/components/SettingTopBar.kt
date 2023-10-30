package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.theme.getTopBarColor

@Composable
fun SettingsTopBar(
    onBack: () -> Unit,
) {
    TopAppBar(
        backgroundColor = getTopBarColor(),
        elevation = MaterialTheme.elevation.extraSmall,
        navigationIcon = {
            BackButton(onClick = onBack)
        },
        title = {
            Title(R.string.settings_screen_title)
        }
    )
}