package com.kappdev.worktracker.tracker_feature.presentation.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kappdev.worktracker.R

@Composable
fun NoDataScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_no_data),
            contentDescription = "no data image",
            modifier = Modifier.size(200.dp).alpha(0.9f)
        )
    }
}