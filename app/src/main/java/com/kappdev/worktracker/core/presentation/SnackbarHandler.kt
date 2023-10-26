package com.kappdev.worktracker.core.presentation

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun InfoSnackbarHandler(
    hostState: SnackbarHostState,
    snackbarState: SnackbarState,
    onDismiss: () -> Unit = {}
) {
    val message = snackbarState.message.collectAsState(initial = "")

    LaunchedEffect(message.value) {
        if (message.value.isNotBlank()) {
            val snackbarResult = hostState.showSnackbar(message.value)
            if (snackbarResult == SnackbarResult.Dismissed) {
                snackbarState.clear()
                onDismiss()
            }
        }
    }
}