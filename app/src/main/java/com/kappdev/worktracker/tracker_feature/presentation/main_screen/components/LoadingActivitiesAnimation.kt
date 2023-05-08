package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.kappdev.worktracker.tracker_feature.presentation.common.components.AnimatedShimmer
import com.kappdev.worktracker.ui.elevation
import com.kappdev.worktracker.ui.spacing

@Composable
fun LoadingActivitiesAnimation() {
    AnimatedShimmer(
        duration = 2_000
    ) { shimmerBrush ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.extraSmall),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            items(20) {
                Item(shimmerBrush)
            }
        }
    }
}

@Composable
private fun Item(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = MaterialTheme.spacing.extraSmall)
            .shadow(elevation = MaterialTheme.elevation.extraSmall)
            .background(MaterialTheme.colors.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.small)
                .fillMaxWidth(0.5f)
                .height(18.dp)
                .background(
                    brush = brush,
                    shape = CircleShape
                )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Timer,
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground
            )

            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
        }
    }
}


























