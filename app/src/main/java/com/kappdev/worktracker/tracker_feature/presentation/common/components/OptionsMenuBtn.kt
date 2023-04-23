package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun OptionsMenuBtn(
    titlesResIds: List<Int>,
    modifier: Modifier = Modifier,
    menuAlignment: Alignment = Alignment.TopEnd,
    icon: ImageVector = Icons.Default.MoreVert,
    intOffset: IntOffset = IntOffset(0,0),
    onItemClick: (titleResId: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier,
        onClick = { expanded = true }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "option menu button",
            tint = MaterialTheme.colors.onSurface
        )
    }

    CustomDropDownMenu(
        expanded = expanded,
        offset = intOffset,
        alignment = menuAlignment,
        modifier = Modifier.width(140.dp),
        dismiss = { expanded = false }
    ) {
        titlesResIds.forEach { titleResId ->
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onItemClick(titleResId)
                }
            ) {
                Text(
                    text = stringResource(titleResId),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}