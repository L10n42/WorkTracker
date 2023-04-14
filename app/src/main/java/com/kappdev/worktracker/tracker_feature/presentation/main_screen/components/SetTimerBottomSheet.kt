package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.core.common.makeToast
import com.kappdev.worktracker.tracker_feature.data.util.ServiceState
import com.kappdev.worktracker.tracker_feature.presentation.common.components.TimerPicker
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenBottomSheet
import com.kappdev.worktracker.tracker_feature.presentation.main_screen.MainScreenViewModel
import com.kappdev.worktracker.ui.spacing

@Composable
fun SetTimerBottomSheet(
    viewModel: MainScreenViewModel,
    countdownState: ServiceState,
    sheet: MainScreenBottomSheet.TimePicker
) {
    var currentTimeInMillis by remember { mutableStateOf(0L) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.background,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(all = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.set_time_title),
            color = MaterialTheme.colors.onSurface,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )

        TimerPicker(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { timeInMillis ->
                currentTimeInMillis = timeInMillis
            }
        )

        VerticalSpace(MaterialTheme.spacing.large)
        Buttons(
            onCancelClick = viewModel::closeSheet,
            onOkClick = {
                if (currentTimeInMillis > 0) {
                    if (countdownState == ServiceState.Idle) {
                        viewModel.countdownController.start(
                            activityId = sheet.activityId,
                            activityName = sheet.activityName,
                            durationInMillis = currentTimeInMillis
                        )
                    }
                    viewModel.closeSheet()
                } else {
                    context.makeToast(R.string.wrong_time_error)
                }
            }
        )
    }
}

@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            title = stringResource(id = R.string.btn_cancel),
            titleColor = MaterialTheme.colors.onSurface,
            onClick = onCancelClick
        )

        Button(
            title = stringResource(id = R.string.btn_ok),
            titleColor = MaterialTheme.colors.primary,
            onClick = onOkClick
        )
    }
}

@Composable
fun Button(
    title: String,
    titleColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier
            .width(150.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface.copy(0.64f)
        )
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = titleColor
        )
    }
}