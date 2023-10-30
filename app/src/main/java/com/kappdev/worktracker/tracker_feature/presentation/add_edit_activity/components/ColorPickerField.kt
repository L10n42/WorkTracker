package com.kappdev.worktracker.tracker_feature.presentation.add_edit_activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kappdev.worktracker.R
import com.kappdev.worktracker.ui.customOutlinedTextFieldColors
import com.kappdev.worktracker.ui.customShape

@Composable
fun ColorPickerField(
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = stringResource(R.string.title_color),
        shape = MaterialTheme.customShape.extraSmall,
        singleLine = true,
        readOnly = true,
        onValueChange = { /* NOTHING */ },
        textStyle = TextStyle(fontSize = 18.sp),
        colors = TextFieldDefaults.customOutlinedTextFieldColors(),
        trailingIcon = {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color, CircleShape)
            )
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                if (state.isFocused) {
                    onClick()
                    focusManager.clearFocus()
                }
            }
    )
}