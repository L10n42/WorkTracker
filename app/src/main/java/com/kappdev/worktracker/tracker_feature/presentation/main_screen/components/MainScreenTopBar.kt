package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Title
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.elevation

@Composable
fun MainScreenTopBar(
    navigate: (route: String) -> Unit,
    openSheet: (sheet: MainScreenBottomSheet) -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = MaterialTheme.elevation.extraSmall,
        title = {
            Title(R.string.main_screen_title)
        },
        actions = {
            IconButton(
                onClick = {
                    navigate(Screen.AddEditActivity.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "button for adding new activities",
                    tint = MaterialTheme.colors.onSurface
                )
            }

            IconButton(
                onClick = {
                    openSheet(MainScreenBottomSheet.Sort)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "button for sorting activities",
                    tint = MaterialTheme.colors.onSurface
                )
            }

            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "button for opening settings",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    )
}