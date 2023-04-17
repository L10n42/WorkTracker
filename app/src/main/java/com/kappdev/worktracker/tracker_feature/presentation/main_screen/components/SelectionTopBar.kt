package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedDigit
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenDialog
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.elevation

@Composable
fun SelectionTopBar(
    viewModel: MainScreenViewModel
) {
    val selectedItems = viewModel.selectedActivities.size
    val allSelected = viewModel.allSelected()
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = MaterialTheme.elevation.extraSmall,
        navigationIcon = {
            IconButton(onClick = viewModel::cancelSelection) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "switch selection mode off",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        title = {
            AnimatedDigit(
                count = selectedItems,
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onSurface
                )
            )
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.openDialog(MainScreenDialog.RemoveActivitiesConf)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteForever,
                    contentDescription = "delete all activities",
                    tint = MaterialTheme.colors.onSurface
                )
            }

            IconButton(
                onClick = {
                    if (allSelected) viewModel.deselectAll() else viewModel.selectAll()
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Checklist,
                    contentDescription = "select all button",
                    tint = if (allSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        }
    )
}