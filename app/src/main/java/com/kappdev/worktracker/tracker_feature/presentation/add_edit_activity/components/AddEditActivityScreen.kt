package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.navigation.Screen
import com.kappdev.worktracker.tracker_feature.domain.model.stringFormat
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivitySheet
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivitySheet.*
import com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.AddEditActivityViewModel
import com.kappdev.worktracker.tracker_feature.presentation.common.components.OutlineTextField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.SelectorField
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
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
    val color = viewModel.color.value

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
    var bottomSheetType by remember { mutableStateOf(IDLE) }

    val scope = rememberCoroutineScope()
    fun hideSheet() = scope.launch {
        bottomSheetState.hide()
        bottomSheetType = IDLE
    }
    fun showSheet(type: AddEditActivitySheet) = scope.launch {
        bottomSheetType = type
        bottomSheetState.show()
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            when (bottomSheetType) {
                COLOR_PICKER -> {
                    ColorPickerSheet { newColor ->
                        viewModel.setColor(newColor)
                        hideSheet()
                    }
                }
                TIME_PICKER -> {
                    SelectTimeBottomSheet(
                        initValue = target,
                        closeSheet = ::hideSheet,
                        onTimeSelected = viewModel::setTarget,
                        title = stringResource(R.string.set_target_title)
                    )
                }
                IDLE -> VerticalSpace(1.dp)
            }
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
                        showSheet(TIME_PICKER)
                    }
                )

                ColorPickerField(
                    color = color,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showSheet(COLOR_PICKER)
                    }
                )
            }
        }
    }
}


























