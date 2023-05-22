package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.presentation.common_components.AnimatedDotsText
import com.kappdev.worktracker.core.presentation.common_components.AnimatedLoadingCircle

@Composable
fun CustomLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedLoadingCircle(
            diameter = 42.dp,
            strokeWidth = 4.dp,
            strokeColor = MaterialTheme.colors.onSurface
        )

        AnimatedDotsText(
            text = stringResource(R.string.loading_title),
            style = TextStyle(fontSize = 22.sp, color = MaterialTheme.colors.onSurface),
            modifier = Modifier.width(100.dp).align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}

@Composable
fun SimpleLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}