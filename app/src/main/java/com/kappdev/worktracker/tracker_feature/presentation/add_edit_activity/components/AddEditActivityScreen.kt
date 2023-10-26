package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.stringFormat
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivityViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.OutlineTextField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.SelectorField
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.components.SelectTimeBottomSheet
import com.kappdev.worktracker.ui.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditActivityScreen(
    navController: NavHostController,
    activityId: Long?,
    viewModel: AddEditActivityViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val target = viewModel.target.value
    val name = viewModel.name.value

    LaunchedEffect(Unit) {
        if (activityId != null && activityId > 0) {
            viewModel.getActivityBy(
                id = activityId,
                onError = { navController.popBackStack() }
            )
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    fun hideSheet() = scope.launch { bottomSheetState.hide() }
    fun showSheet() = scope.launch { bottomSheetState.show() }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            SelectTimeBottomSheet(
                initValue = target,
                closeSheet = ::hideSheet,
                onTimeSelected = viewModel::setTarget,
                title = stringResource(R.string.set_target_title)
            )
        }
    ) {
        Scaffold(
            topBar = {
                AddEditActivityTopBar(viewModel) {
                    navController.navigate(Screen.Main.route)
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                DoneButton {
                    if (viewModel.canSave()) {
                        viewModel.save()
                        navController.navigate(Screen.Main.route)
                    } else {
                        viewModel.detectErrorAndShowToast()
                    }
                }
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                OutlineTextField(
                    value = name,
                    label = stringResource(R.string.label_name),
                    hint = stringResource(R.string.name_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                    onValueChanged = {
                        viewModel.setName(it)
                    }
                )

                SelectorField(
                    value = target.stringFormat().ifBlank { stringResource(R.string.target_placeholder) },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.label_target),
                    onClick = {
                        focusManager.clearFocus()
                        showSheet()
                    }
                )
            }
        }
    }
}


























