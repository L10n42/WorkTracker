package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.theme.getTopBarColor

@Composable
fun MainScreenTopBar(
    onNavigate: (route: String) -> Unit,
    openSheet: (sheet: MainScreenBottomSheet) -> Unit
) {
    TopAppBar(
        backgroundColor = getTopBarColor(),
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(R.string.main_screen_title)
        },
        actions = {
            ActionButton(icon = Icons.Default.Add) {
                onNavigate(Screen.AddEditActivity.route)
            }

            ActionButton(icon = Icons.Outlined.Analytics) {
                onNavigate(Screen.WorkStatistic.route)
            }

            ActionButton(icon = Icons.Default.Sort) {
                openSheet(MainScreenBottomSheet.Sort)
            }

            ActionButton(icon = Icons.Default.Settings) {
                onNavigate(Screen.Settings.route)
            }
        }
    )
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = "action button ${icon.name}",
            tint = MaterialTheme.colors.onSurface
        )
    }
}