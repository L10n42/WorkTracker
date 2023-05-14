package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.theme.DarkGray
import com.kappdev.worktracker.ui.theme.SuperDarkGray

@Composable
fun MainScreenTopBar(
    navigate: (route: String) -> Unit,
    openSheet: (sheet: MainScreenBottomSheet) -> Unit
) {
    TopAppBar(
        backgroundColor = SuperDarkGray,
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(R.string.main_screen_title)
        },
        actions = {
            ActionButton(icon = Icons.Default.Add) {
                navigate(Screen.AddEditActivity.route)
            }

            ActionButton(icon = Icons.Outlined.Analytics) {
                navigate(Screen.WorkStatistic.route)
            }

            ActionButton(icon = Icons.Default.Sort) {
                openSheet(MainScreenBottomSheet.Sort)
            }

            ActionButton(icon = Icons.Default.Settings) {
                navigate(Screen.Settings.route)
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