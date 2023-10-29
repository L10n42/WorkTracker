package com.kappdev.worktracker.tracker_feature.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingItem(
    titleRes: Int,
    descriptionRes: Int? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(titleRes),
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )

            if (descriptionRes != null) {
                Text(
                    text = stringResource(descriptionRes),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        Icon(
            imageVector = Icons.Rounded.ArrowForwardIos,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.size(18.dp),
            contentDescription = null
        )
    }
}