package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R

@Composable
fun Title(resId: Int) = Title(stringResource(resId))

@Composable
fun Title(
    value: String
) {
    Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}