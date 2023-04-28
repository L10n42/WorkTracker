package com.kappdev.worktracker.tracker_feature.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
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
import com.kappdev.worktracker.tracker_feature.domain.model.Time
import com.kappdev.worktracker.tracker_feature.domain.model.inMillis
import com.kappdev.worktracker.tracker_feature.presentation.common.components.TimerPicker
import com.kappdev.worktracker.tracker_feature.presentation.common.components.VerticalSpace
import com.kappdev.worktracker.ui.spacing

@Composable
fun SelectTimeBottomSheet(
    initValue: Time = Time(),
    title: String = stringResource(R.string.set_time_title),
    closeSheet: () -> Unit,
    onTimeSelected: (time: Time) -> Unit
) {
    var currentTime by remember { mutableStateOf(initValue) }
    var isCommonTimeVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = initValue) {
        currentTime = initValue
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.background,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(MaterialTheme.spacing.medium)
    ) {
        SheetHat(
            title = title,
            isSectionVisible = isCommonTimeVisible,
            modifier = Modifier.fillMaxWidth(),
            showSection = { isCommonTimeVisible = !isCommonTimeVisible }
        )

        TimerPicker(
            modifier = Modifier.fillMaxWidth(),
            defaultValue = currentTime,
            onValueChange = { newTime ->
                currentTime = newTime
            }
        )

        VerticalSpace(MaterialTheme.spacing.medium)
        CommonTimePicker(
            isVisible = isCommonTimeVisible,
            modifier = Modifier.fillMaxWidth(),
            onTimePicked = { pickedTime ->
                currentTime = pickedTime
            }
        )

        VerticalSpace(MaterialTheme.spacing.large)
        Buttons(
            onCancelClick = closeSheet,
            onOkClick = {
                if (currentTime.inMillis() > 0) {
                    onTimeSelected(currentTime)
                    closeSheet()
                } else {
                    context.makeToast(R.string.wrong_time_error)
                }
            }
        )
    }
}

@Composable
private fun SheetHat(
    title: String,
    modifier: Modifier = Modifier,
    isSectionVisible: Boolean,
    showSection: () -> Unit
) {
    Box(modifier = modifier) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSurface,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = showSection,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "show common time section",
                tint = if (isSectionVisible) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
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