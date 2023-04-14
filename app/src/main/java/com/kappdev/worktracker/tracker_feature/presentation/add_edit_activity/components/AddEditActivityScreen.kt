package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivityViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.Error
import com.kappdev.worktracker.tracker_feature.presentation.common.components.TextField
import com.kappdev.worktracker.ui.spacing

@Composable
fun AddEditActivityScreen(
    navController: NavHostController,
    activityId: Long?,
    viewModel: AddEditActivityViewModel = hiltViewModel()
) {
    val navigate = viewModel.navigate.value

    LaunchedEffect(key1 = true) {
        if (activityId != null && activityId > 0) {

        }
    }

    LaunchedEffect(key1 = navigate) {
        if (navigate != null) {
            navController.navigate(navigate)
            viewModel.clearNavigationRoute()
        }
    }

    Scaffold(
        topBar = {
            AddEditActivityTopBar(viewModel)
        }
    ) { scaffoldPadding ->
        ScreenContent(viewModel, Modifier.padding(scaffoldPadding))
    }
}

@Composable
private fun ScreenContent(
    viewModel: AddEditActivityViewModel,
    modifier: Modifier = Modifier
) {
    val name = viewModel.name.value
    val error = viewModel.error.value

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TextField(
            value = name,
            hint = stringResource(R.string.hint_name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MaterialTheme.spacing.medium),
            onValueChanged = {
                viewModel.setName(it)
            }
        )

        AnimatedVisibility(
            visible = error != null,
            enter = slideInVertically() + fadeIn(),
            exit = fadeOut()
        ) {
            error?.let {
                Error(
                    message = error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                    onDismiss = viewModel::hideError
                )
            }
        }
    }
}


























