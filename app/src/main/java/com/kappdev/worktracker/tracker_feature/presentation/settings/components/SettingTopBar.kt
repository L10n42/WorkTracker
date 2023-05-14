package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.BackButton
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.theme.SuperDarkGray

@Composable
fun SettingsTopBar(
    navigate: (route: String) -> Unit,
) {
    TopAppBar(
        backgroundColor = SuperDarkGray,
        elevation = MaterialTheme.elevation.extraSmall,
        navigationIcon = {
            BackButton {
                 navigate(Screen.Main.route)
            }
        },
        title = {
            Title(R.string.settings_screen_title)
        }
    )
}